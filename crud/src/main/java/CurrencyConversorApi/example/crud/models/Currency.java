package CurrencyConversorApi.example.crud.models;

import joinery.DataFrame;

import java.io.IOException;

public class Currency {

    static void GetCurrencyRate(String date, String Currency1, String Currency2, DataFrame df)
    {
        float Currency1_to_reference_rate = Float.parseFloat(
                df.get(date, Currency1).toString());
        float Currency2_to_reference_rate = Float.parseFloat(
                df.get(date, Currency2).toString());
        System.out.println(Currency1_to_reference_rate/Currency2_to_reference_rate);

    }
}
