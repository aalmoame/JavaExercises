package com.example.lambdas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaPrinters {
    public static void main(String[] args) {
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

        //reverse sort list by names
        System.out.println("Sorted List:");
        List<Printer> sortedList = printers.stream()
            .sorted(new Comparator<Printer>() {

                @Override
                public int compare(Printer arg0, Printer arg1) {
                    return arg1.getName().compareTo(arg0.getName());
                }
            
            })
            .peek(printer -> System.out.println(printer.getName()))
            .toList();

        //filter list by beginning with 'A' and less than 30 sps
        System.out.println("\n Filtered List");
        List<Printer> filteredList = printers.stream()
            .filter(new Predicate<Printer>() {
                @Override
                public boolean test(Printer printer)
                {
                    return !printer.getName().startsWith("A") || printer.getSpeedPerSecond() >= 30;
                }
            })
            .peek(printer -> System.out.println(printer.getName()))
            .toList();

        //reverse sort list by name using a lambda runnable
        System.out.println("\n Sorted List Using Runnable");
        List<Printer> sortedListUsingRunnable = printers.stream()
            .sorted((printer0, printer1) -> {
                return printer1.getName().compareTo(printer0.getName()); 
            })
            .peek(printer -> System.out.println(printer.getName()))
            .toList();

        //filter list by beggining with 'A' and less than 30 sps
        System.out.println("\n Filtered List Using Runnable");
        List<Printer> filteredListUsingRunnable = printers.stream()
            .filter(printer -> !printer.getName().startsWith("A") || printer.getSpeedPerSecond() >= 30)
            .peek(printer -> System.out.println(printer.getName()))
            .toList();

        
        //collecting disting speed per second
        System.out.println("\n Distinct Speeds");
        Set<Integer> speeds = new HashSet<>(printers.size());
        printers.stream().filter(printer -> speeds.add(printer.getSpeedPerSecond())).collect(Collectors.toList());
        List<Integer> distinctSpeeds = new ArrayList<>(speeds);
        System.out.println(distinctSpeeds);

        //sort by Type:
        System.out.println("\n Sorted List by Type");
        List<Printer> sortedListByType = printers.stream()
        .sorted((printer0, printer1) -> {
            return printer0.getType().toString().compareTo(printer1.getType().toString()); 
        })
        .peek(printer -> System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType()))
        .toList();

        //Top 3 elements from list sorted by type
        System.out.println("\n Top 3 Elements in List Sorted by Type");
        List<Printer> top3List = sortedListByType.subList(0, 3);
        top3List.forEach(printer -> System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType()));

        //Finding the Fastest printer
        System.out.println("\n Fastest Printer");
        Optional<Printer> fastestPrinter = printers.stream().max(Comparator.comparing(Printer::getSpeedPerSecond));
        System.out.println("Name: " + fastestPrinter.get().getName() + ", Type: " + fastestPrinter.get().getType() + ", Speed: " + fastestPrinter.get().getSpeedPerSecond());


        //Using method1 with consumer to find canon printers
        System.out.println("\n Finding all Canon Printers");
        method1(printers, printer -> {
            if(printer.getName().startsWith("Canon")) {
                System.out.println("Name: " + printer.getName() + ", Type: " + printer.getType());
            }
        });

        //using method2 with predicate to filter out non laser printers slower than 20 sps
        System.out.println("\n Collecting all laser printers faster than 20 sps");
        List<Printer> fastLaserPrinters = method2(printers, printer -> printer.getType().equals(Printer.Type.Laser) && printer.getSpeedPerSecond() > 20);

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
