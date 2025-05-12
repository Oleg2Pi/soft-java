package com.polikarpov.softjava.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FileService {


    public Integer findMinNumber(String filePath, Integer n) {
        try {
            List<Integer> numbers = readNumbersFromXLSX(filePath);
            if (numbers.isEmpty()) {
                throw new NoSuchElementException("В файле нет чисел");
            }

            if (numbers.size() < n) {
                throw new NoSuchElementException(String.format("В файле количество чисел меньше, чем %d", n));
            }

            return selectMinNumber(numbers, n);
        } catch (IOException exception) {
            throw new NoSuchElementException(exception.getMessage());
        }
    }

    private int selectMinNumber(List<Integer> numbers, int n) {
        int[] arr = numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        return quickSelect(arr, 0, arr.length - 1, n);
    }

    private int quickSelect(int[] arr, int left, int right, int n) {
        if (left == right) {
            return arr[left];
        }

        int pivotIndex = partition(arr, left, right);
        int k = pivotIndex - left + 1;

        if (n == k) {
            return arr[pivotIndex];
        } else if (n < k) {
            return quickSelect(arr, left, pivotIndex - 1, n);
        } else {
            return quickSelect(arr, pivotIndex + 1, right, n - k);
        }

    }

    private int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] < pivot) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
            }
        }

        int tmp = arr[i];
        arr[i] = arr[right];
        arr[right] = tmp;
        return i;
    }

    private List<Integer> readNumbersFromXLSX(String filePath) throws IOException {
        List<Integer> numbers = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    numbers.add((int) cell.getNumericCellValue());
                }
            }
        }

        return numbers;

    }
}
