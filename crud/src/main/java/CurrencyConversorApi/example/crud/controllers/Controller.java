package CurrencyConversorApi.example.crud.controllers;

import joinery.DataFrame;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/currency")
public class Controller {
private DataFrame df = new DataFrame();
    public Controller() {
        try {
            df = df.readCsv("C:\\Users\\Marto\\OneDrive\\Escritorio\\cositas\\java\\crud\\src\\main\\java\\2023.csv");
            df = df.reindex(0, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping(path="/{date}/{Currency1}/{Currency2}")
    public float GetCurrencyRate(@PathVariable String  date, @PathVariable String Currency1, @PathVariable String Currency2)
    {
        System.out.println(date+" "+Currency1+" "+Currency2);
        float Currency1_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency1).toString());
        float Currency2_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency2).toString());
        return (Currency1_to_reference_rate/Currency2_to_reference_rate);

    }

}
