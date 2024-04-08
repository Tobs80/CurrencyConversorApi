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
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/conversion")
public class Controller {
private DataFrame df = new DataFrame();
private List currency_codes;
    public Controller() {
        try {
            df = df.readCsv("C:\\Users\\Marto\\OneDrive\\Escritorio\\cositas\\java\\crud\\src\\main\\java\\2023.csv");
            df = df.reindex(0, true);
            currency_codes = Arrays.asList(df.columns().toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Invalid URI format. Expected /date(yyyymmdd)-Currency_Code_base-Currency_code_objective")  // 500
    public class URL_error extends RuntimeException {
        // ...
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No data available for this date or before")
    public class No_data_date_error extends RuntimeException {
        // ...
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="One of the currency codes was not found in database.")
    public class currency_code_error_not_found extends RuntimeException {
        // ...
    }



    @GetMapping(path="/{date}-{Currency_Code_1}-{Currency_Code_2}")
    public float GetCurrencyRate(@PathVariable String  date, @PathVariable String Currency_Code_1, @PathVariable String Currency_Code_2)
    {
        Currency_Code_1 = Currency_Code_1.toUpperCase();
        Currency_Code_2 = Currency_Code_2.toUpperCase();
        date = CheckDate(date);
        check_currency_codes(Currency_Code_1,Currency_Code_2);
        float Currency1_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency_Code_1).toString());
        float Currency2_to_reference_rate = Float.parseFloat(
                this.df.get(date, Currency_Code_2).toString());

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
            catch (java.lang.StackOverflowError O)
            {
                throw new No_data_date_error();
            }
            catch (java.text.ParseException Pe)
            {
                throw new URL_error();
            }


        }

    }

    private void check_currency_codes(String currency1, String currency2)
    {
        if (currency1.length()!=3  || currency2.length()!=3)
        {
            throw new URL_error();
        }
        System.out.print((currency_codes));
        if (!currency_codes.contains(currency1) || !currency_codes.contains(currency2))
        {
            throw new currency_code_error_not_found();
        }

    }



}
