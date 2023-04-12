package com.greenblat.naumentask.reader;

import java.util.List;

public interface Reader<T> {

    List<T> readFile();
}
