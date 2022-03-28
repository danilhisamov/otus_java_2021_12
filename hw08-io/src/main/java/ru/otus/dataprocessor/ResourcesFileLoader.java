package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class ResourcesFileLoader implements Loader {
    private final File file;

    public ResourcesFileLoader(String fileName) {
        this.file = checkIfExistsAndReturn(fileName);
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (var fr = new FileReader(file)) {
            return new Gson().fromJson(fr, new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }

    private File checkIfExistsAndReturn(String fileName) {
        return Optional.of(this)
                .map(Object::getClass)
                .map(Class::getClassLoader)
                .map(cl -> cl.getResource(fileName))
                .map(URL::getPath)
                .map(File::new)
                .orElseThrow(() -> new FileProcessException(fileName));
    }
}
