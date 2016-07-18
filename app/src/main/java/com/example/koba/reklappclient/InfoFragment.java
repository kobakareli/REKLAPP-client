package com.example.koba.reklappclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Koba on 18/07/2016.
 */
public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_layout, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        String info1 = "გასაცემი თანხის ექვივალენტური თანხა გაიცემა <font color='#EE0000'>ბიტკოინებში</font>. <font color='#EE0000'>ბიტკოინი</font> არის ციფრული ვალუტა,\n" +
                "                            რომელსაც, როგორც ჩვეულებრივ ვალუტას, აქვს გაცვლითი კურსი სხვა ვალუტებთან. თქვენ\n" +
                "                            მოახერხებთ თანხის <font color='#EE0000'>emoney</font>-ის\n" +
                "                            ექაუნთზე გადატანას და იქიდან ფულის გამოყენებას ლარში ან\n" +
                "                            ნებისმიერ სხვა ვალუტაში.\n" +
                "                            ბიტკოინის უპირატოსობა ისაა, რომ საბანკო ოპერაციებისგან განსხვავებით გადარიცხვები არის\n" +
                "                            უფასო.\n" +
                "                            იმისათვის, რომ თანხა მიიღოთ, აუცილებელია <font color='#EE0000'>coinbase</font>-ზე\n" +
                "                            რეგისტრაცია. რეგისტრაცია უფასოა.\n";
        String info2 = "<font color='#FF8F00'>როგორ დავრეგისტრირდეთ coinbase-ზე</font>?\n" +
                "                            შევდივართ\n" +
                "                            <font color='#EE0000'>www.coinbase.com</font>-ზე.\n" +
                "                            მარჯვნივ\n" +
                "                            ზემოთ ვაწვებით sign up.\n" +
                "                            შეგვყავს\n" +
                "                            სახელი, გვარი, იმეილის მისამართი და პაროლი, ვეთანხმებით პირობებს და ვაჭერთ\n" +
                "                            CREATE ACCOUNT.\n" +
                "                            ახლა\n" +
                "                            შედით თქვენს იმეილზე და <font color='#EE0000'>coinbase</font>-იდან\n" +
                "                            მოსულ ბმულზე დაკლიკეთ.\n" +
                "                            გილოცავთ! თქვენ წარმატებით დარეგისტრირდით <font color='#EE0000'>coinbase</font>-ზე.\n" +
                "                            <font color='#EE0000'>coinbase</font>-ზე\n" +
                "                            რეგისტრაციისას რა\n" +
                "                            მეილიც მიუთითეთ, იგივე უნდა მიუთითოთ თანხის გატანისას.\n";
        String info3 = "<font color='#FF8F00'>როგორ გამოვიტანო coinbase-იდან\n" +
                "                            ფული?</font>\n\n" +
                "                            ამისათვის საჭიროა <font color='#EE0000'>www.emoney.ge</font>-ზე\n" +
                "                            ანგარიშის ქონა. <font color='#EE0000'>emoney</font>-ზე\n" +
                "                            რეგისტრაცია უფასოა.\n" +
                "                            დარეგისტრირდით\n" +
                "                            და შედით თქვენს ექაუნთზე.\n" +
                "                            მარჯვნივ\n" +
                "                            ზემოთ დააჭირეთ ბიტკოინებს.\n" +
                "                            მარცხნივ\n" +
                "                            დაკლიკეთ ჩემი ბიტკოინის მისამართზე.\n" +
                "                            დააკოპირეთ\n" +
                "                            თქვენი ბიტკოინის მისამართი.\n" +
                "                            შედით\n" +
                "                            <font color='#EE0000'>www.coinbase.com</font>--ზე\n" +
                "                            თქვენს ექაუნთზე.\n" +
                "                            მარცხნივ\n" +
                "                            დააჭირეთ Send/Request-ს.\n" +
                "                            ზემოთ აირჩიეთ send.\n" +
                "                            Recipient-ში\n" +
                "                            ჩაწერეთ <font color='#EE0000'>emoney</font>-დან\n" +
                "                            წეღან დაკოპირებული მისამართი, ქვემოთ კი მიუთითეთ\n" +
                "                            გაგზავნილი თანხის ოდენობა.\n" +
                "                            დააჭირეთ\n" +
                "                            გაგზავნას.\n" +
                "                            დაახლოებით 1 საათში გაგზავნილი ბიტკოინები აღმოჩნდება თქვენს <font color='#EE0000'>emoney</font>-ს\n" +
                "                            ანგარიშზე. აქედან\n" +
                "                            კი გნებავთ ტელეფონზე ჩაირიცხეთ ფული, გნებავთ დენის ფული გადაიხადეთ ან თქვენს საბანკო\n" +
                "                            ანგარიშზე გადარიცხეთ :)";
        TextView infoText1 = (TextView) rootView.findViewById(R.id.info_text_1);
        TextView infoText2 = (TextView) rootView.findViewById(R.id.info_text_2);
        TextView infoText3 = (TextView) rootView.findViewById(R.id.info_text_3);
        infoText1.setText(Html.fromHtml(info1));
        infoText2.setText(Html.fromHtml(info2));
        infoText3.setText(Html.fromHtml(info3));
        return rootView;
    }

}
