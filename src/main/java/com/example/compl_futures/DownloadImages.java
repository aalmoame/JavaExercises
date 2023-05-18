package com.example.compl_futures;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

public class DownloadImages {
    public static void main(String[] args) {
        AtomicInteger errorCount = new AtomicInteger();

        List<String> urls = getHundredUrls("/urls000");

        List<CompletableFuture<File>> completableFutures = new ArrayList<>();
         urls.forEach(url -> {
             CompletableFuture<File> completableFuture = CompletableFuture
                     .supplyAsync(() -> downloadImage(url))
                     .thenApply(file -> compressGZip(file))
                     .thenApply(file -> moveFileToFinalDir(file, Path.of("ComplFutures/images/")))
                     .thenApply(file -> printFileSize(file))
                     .handle((file, throwable) -> {
                         if (throwable != null) {
                             System.out.println("Error: " + throwable.getMessage());
                             System.out.println("Error Count is at: " + errorCount.incrementAndGet());
                         }
                         return file;
                     });
             completableFutures.add(completableFuture);
         });

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]))
                .exceptionally(ex -> null)
                .join();
    }

    private static List<String> getHundredUrls(String fileName) {
        List<String> urls = new ArrayList<>();
        int counter = 0;
        try {
            Scanner scanner = new Scanner(DownloadImages.class.getResource(fileName).openStream());
            while (scanner.hasNextLine() && counter < 100) {
                urls.add(scanner.nextLine());
                counter++;
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    private static File downloadImage(String url) {
        try(InputStream in = new URL(url).openStream()) {
            Path tempPath = Files.createTempFile(null, null);
            Files.copy(in, tempPath, StandardCopyOption.REPLACE_EXISTING);
            return tempPath.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File compressGZip(File fileToCompress) {
        Path outputPath = null;
        try {
            outputPath = Files.createTempFile(null, ".gzip");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            byte[] allBytes = Files.readAllBytes(fileToCompress.toPath());
            gzipOutputStream.write(allBytes);
            return outputPath.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File moveFileToFinalDir(File fileToMove, Path finalDest) {
        finalDest.toFile().mkdirs();
        try {
            return Files.move(fileToMove.toPath(), Path.of(finalDest + "/" + fileToMove.getName()), StandardCopyOption.REPLACE_EXISTING).toFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File printFileSize(File file) {
        try {
            System.out.println(file.getName() + " Details:");
            System.out.println("Size of File: " + Files.size(file.toPath()) + " Bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
