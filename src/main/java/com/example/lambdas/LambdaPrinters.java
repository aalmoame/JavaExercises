package com.example.lambdas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LambdaPrinters {
    public static void main(String[] args) throws InterruptedException {
        List<Printer> printers = new ArrayList<>();

        printers.add(new Printer("HP OfficeJet Pro", Printer.Type.ThreeDimensional, 50));
        printers.add(new Printer("Canon Maxify", Printer.Type.LED, 25));
        printers.add(new Printer("Brother MFC-J4335DW", Printer.Type.Laser, 45));
        printers.add(new Printer("Epson EcoTank Pro ET-5850", Printer.Type.DotMatrix, 60));
        printers.add(new Printer("Amazing Color LaserJet Enterprise MFP M480f", Printer.Type.Laser, 20));
        printers.add(new Printer("Canon imageClass MF452dw", Printer.Type.LED, 45));
        printers.add(new Printer("HP Neverstop Laser MFP 1202w", Printer.Type.Laser, 30));
        printers.add(new Printer("AwesomePrinter HL-L9310CDW", Printer.Type.ThreeDimensional, 25));
        printers.add(new Printer("Canon ImageClass LBP236dw", Printer.Type.ThreeDimensional, 55));
        printers.add(new Printer("Brother MFC-J6945DW", Printer.Type.DotMatrix, 30));

        System.out.println(" Sorted List:");
        List<Printer> sortedList = printers.stream()
            .sorted((printer1, printer2) -> printer2.getName().compareTo(printer1.getName()))
            .peek(printer -> System.out.println(printer.getName()))
            .toList();

        //filter list by beginning with 'A' and less than 30 sps
        System.out.println("\n Filtered List");
        List<Printer> filteredList = printers.stream()
            .filter(printer -> !printer.getName().startsWith("A") || printer.getSpeedPerSecond() >= 30)
            .peek(printer -> System.out.println(printer.getName()))
            .toList();
        

        Thread thread = new Thread(() -> System.out.println("\n I'm in a lambda runnable"));

        thread.start();
        thread.join();
        
        Thread th1 = new Thread(() -> sort(printers));
        Thread th2 = new Thread(() -> filter(printers));

        //reverse sort list by name using a lambda runnable
        th1.start();
        th1.join();

        //filter list using a lambda runnable
        th2.start();
        th2.join();
        
        //collecting disting speed per second
        System.out.println("\n Distinct Speeds");
        Set<Integer> distinctSpeeds = printers.stream().map(Printer::getSpeedPerSecond).distinct().collect(Collectors.toSet());
        System.out.println(distinctSpeeds);

        //sort by Type:
        System.out.println("\n Sorted List by Type");
        List<Printer> sortedListByType = printers.stream()
        .peek(printer -> System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType()))
        .peek(System.out::println)
        .toList();

        //Top 3 elements from list sorted by type
        System.out.println("\n Top 3 Elements in List Sorted by Type");
        List<Printer> top3List = sortedListByType.stream().limit(3).toList();
        top3List.forEach(printer -> System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType()));

        //Finding the Fastest printer
        System.out.println("\n Fastest Printer");
        Optional<Printer> fastestPrinter = printers.stream().max(Comparator.comparing(Printer::getSpeedPerSecond));
        System.out.println("Name: " + fastestPrinter.get().getName() + ", Type: " + fastestPrinter.get().getType() + ", Speed: " + fastestPrinter.get().getSpeedPerSecond());


        //Using method1 with consumer to find canon printers
        System.out.println("\n Finding all Canon Printers");
        method1(printers, (printer) -> System.out.println(printer.getType().toString()));

        //using method2 with predicate to filter out non laser printers slower than 20 sps
        System.out.println("\n Collecting all laser printers faster than 20 sps");
        List<Printer> fastLaserPrinters = method2(printers, printer -> printer.getType().equals(Printer.Type.Laser) && printer.getSpeedPerSecond() > 20);



        System.out.println("\n Converting printer names to upper case");
        // printers.replaceAll(printer -> new Printer(printer.getName().toUpperCase(), printer.getType(), printer.getSpeedPerSecond()));
        
        printers.stream().map(printer -> {
            return new Printer(printer.getName().toUpperCase(), printer.getType(), printer.getSpeedPerSecond());
        }).peek(printer -> System.out.println("Name: " + printer.getName() + ", Speed: " + printer.getSpeedPerSecond()));
        
        System.out.println("\n Removing Epson Printers");
        printers.removeIf(printer -> printer.getName().startsWith("EPSON"));
        printers.forEach(printer -> System.out.println("Name: " + printer.getName() + ", Speed: " + printer.getSpeedPerSecond()));

        System.out.println("\n Removing Slowest Printer");
        printers.stream().min(Comparator.comparing(Printer::getSpeedPerSecond)).ifPresent(printer -> printers.remove(printer));
        printers.forEach(printer -> System.out.println("Name: " + printer.getName() + ", Speed: " + printer.getSpeedPerSecond()));

        // printers.stream().map(Printer::getType).filter(printerType -> printerType.equals(Printer.Type.LED))
    }

    public static void sort(List<Printer> printers) {
        System.out.println("\n Sorted List Using Runnable");
        printers.stream()
            .sorted((printer1, printer2) -> printer2.getName().compareTo(printer1.getName()))
            .peek(printer -> System.out.println(printer.getName()))
            .toList();
    }

    public static void filter(List<Printer> printers) {
        System.out.println("\n Filtered List Using Runnable");
        printers.stream()
            .filter(printer -> !printer.getName().startsWith("A") || printer.getSpeedPerSecond() >= 30)
            .peek(printer -> System.out.println(printer.getName()))
            .toList();
    }

    public static void method1(List<Printer> printers, Consumer<Printer> printerConsumer) {
        printers.stream().forEach(printerConsumer);
    }

    public static List<Printer> method2(List<Printer> printers, Predicate<Printer> printerPredicate) {
        return printers.stream()
            .filter(printerPredicate)
            .peek(printer -> System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType() + ", Speed: " + printer.getSpeedPerSecond()))
            .toList();
    }
}
