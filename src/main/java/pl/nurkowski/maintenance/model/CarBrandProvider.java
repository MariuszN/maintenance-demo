package pl.nurkowski.maintenance.model;

import java.io.IOException;
import java.util.List;

public interface CarBrandProvider {
    List<String> getValidBrands() throws IOException;
}
