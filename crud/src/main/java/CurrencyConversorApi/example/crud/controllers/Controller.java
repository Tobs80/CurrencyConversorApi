package CurrencyConversorApi.example.crud.controllers;

import joinery.DataFrame;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.time.DateUtils;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/conversion")
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
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Invalid URI format. Expected /date(yyyymmdd)-Currency base-Currency_objective")  // 404
    public class URL_error extends RuntimeException {
        // ...
    }



    @GetMapping(path="/{date}-{Currency1}-{Currency2}")
    public float GetCurrencyRate(@PathVariable String  date, @PathVariable String Currency1, @PathVariable String Currency2)
    {
        System.out.println(date+" "+Currency1.toUpperCase()+" "+Currency2);
        date = CheckDate(date);
        float Currency1_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency1.toUpperCase()).toString());
        float Currency2_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency2.toUpperCase()).toString());

        return (Currency1_to_reference_rate/Currency2_to_reference_rate);

    }
    @GetMapping(path="/**")
    public void ReturnError()
    {
        throw new URL_error();
    }

    private String CheckDate(String date) {
        try{
            this.df.get(date, "USD");
            return date;
        }
          catch (java.lang.IllegalArgumentException e){

            try {
                SimpleDateFormat date_format = new SimpleDateFormat("yyyymmdd");
                Date new_date = date_format.parse(date);
                new_date = DateUtils.addDays(new_date, -1);
                date = CheckDate(date_format.format(new_date));
                return date;

            }
            catch (Exception ee)
            {
                throw new URL_error();
            }

        }
    }



}
