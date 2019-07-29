package com.example.arthi.dmssmartapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arthi.dmssmartapp.OneHouseholdData.Female;
import com.example.arthi.dmssmartapp.OneHouseholdData.FemaleAddapter;
import com.example.arthi.dmssmartapp.OneHouseholdData.HouseholdDate;
import com.example.arthi.dmssmartapp.OneHouseholdData.Male;
import com.example.arthi.dmssmartapp.OneHouseholdData.MaleAddapter;
import com.example.arthi.dmssmartapp.OneHouseholdData.OnItemClickOne;

import java.util.ArrayList;

public class OneHousehold extends AppCompatActivity implements OnItemClickOne {

    EditText householdHead;
    EditText gender;
    EditText age;
    EditText maritalStatus;
    EditText education;
    EditText ethnicGroup;
    EditText regularWages;
    EditText dailyWages;
    EditText nonwagedEarnings;
    EditText seasonalEarnings;
    EditText sourceOfIncome;
    EditText mainIncome;
    EditText secondIncome;
    EditText fullTimeMale;
    EditText fullTimeFemale;
    EditText partTimeMale;
    EditText partTimeFemale;
    EditText totalHouseHold;
    EditText governmentService;
    EditText privateSector;
    EditText services;
    EditText trade;
    EditText construction;
    EditText agriculture;
    EditText casuallabor;
    EditText totalother;
    EditText eaarned;
    EditText govPension;
    EditText govAssistance;
    EditText remittances;
    EditText rentalIncome;
    EditText nonearnedOthers;
    EditText source;
    EditText vegetables;
    EditText rice;
    EditText otherCrop;
    EditText livestock;
    EditText poultry;
    EditText forestProducts;
    EditText handicrafts;
    EditText estimateOthers;
    EditText house;
    EditText roof;
    EditText walls;
    EditText floor;
    EditText disabledMale;
    EditText disabledFemale;
    EditText disabledillness;
    EditText fmailyLiving;
    MaleAddapter maleAddapter;
    FemaleAddapter femaleAddapter;


    private RecyclerView maleList;
    private ArrayList<Male> maleArrayList = new ArrayList<Male>();

    private RecyclerView femaleList;
    private ArrayList<Female> femaleArrayList = new ArrayList<Female>();
    TextView submit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.householddate);

       householdHead = (EditText) findViewById(R.id.householdHead);
       gender = (EditText) findViewById(R.id.gender);
       age = (EditText) findViewById(R.id.age);
       maritalStatus = (EditText) findViewById(R.id.maritalStatus);
       education = (EditText) findViewById(R.id.education);
       ethnicGroup = (EditText) findViewById(R.id.ethnicGroup);
       regularWages = (EditText) findViewById(R.id.regularWages);
       dailyWages = (EditText) findViewById(R.id.dailyWages);
       nonwagedEarnings = (EditText) findViewById(R.id.nonwagedEarnings);
       seasonalEarnings = (EditText) findViewById(R.id.seasonalEarnings);
       sourceOfIncome = (EditText) findViewById(R.id.sourceOfIncome);
       mainIncome = (EditText) findViewById(R.id.mainIncome);
       secondIncome = (EditText) findViewById(R.id.secondIncome);
       fullTimeMale = (EditText) findViewById(R.id.fullTimeMale);
       fullTimeFemale = (EditText) findViewById(R.id.fullTimeFemale);
       partTimeMale = (EditText) findViewById(R.id.partTimeMale);
       partTimeFemale = (EditText) findViewById(R.id.partTimeFemale);
       totalHouseHold = (EditText) findViewById(R.id.totalHouseHold);
       governmentService = (EditText) findViewById(R.id.governmentService);
       privateSector = (EditText) findViewById(R.id.privateSector);
       services = (EditText) findViewById(R.id.services);
       trade = (EditText) findViewById(R.id.trade);
       construction = (EditText) findViewById(R.id.construction);
       agriculture = (EditText) findViewById(R.id.agriculture);
       casuallabor = (EditText) findViewById(R.id.casuallabor);
       totalother = (EditText) findViewById(R.id.totalother);
       eaarned = (EditText) findViewById(R.id.eaarned);
       govPension = (EditText) findViewById(R.id.govPension);
       govAssistance = (EditText) findViewById(R.id.govAssistance);
       remittances = (EditText) findViewById(R.id.remittances);
       rentalIncome = (EditText) findViewById(R.id.rentalIncome);
       nonearnedOthers = (EditText) findViewById(R.id.nonearnedOthers);
       source = (EditText) findViewById(R.id.source);
       vegetables = (EditText) findViewById(R.id.vegetables);
       rice = (EditText) findViewById(R.id.rice);
       otherCrop = (EditText) findViewById(R.id.otherCrop);
       livestock = (EditText) findViewById(R.id.livestock);
       poultry = (EditText) findViewById(R.id.poultry);
       forestProducts = (EditText) findViewById(R.id.forestProducts);
       handicrafts = (EditText) findViewById(R.id.handicrafts);
       estimateOthers = (EditText) findViewById(R.id.estimateOthers);
       house = (EditText) findViewById(R.id.house);
       roof = (EditText) findViewById(R.id.roof);
       walls = (EditText) findViewById(R.id.walls);
       floor = (EditText) findViewById(R.id.floor);
       disabledMale = (EditText) findViewById(R.id.disabledMale);
       disabledFemale = (EditText) findViewById(R.id.disabledFemale);
       disabledillness = (EditText) findViewById(R.id.disabledillness);
       fmailyLiving = (EditText) findViewById(R.id.fmailyLiving);


        submit = (TextView) findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HouseholdDate tempHouseholdDate = new HouseholdDate(

                        householdHead.getText().toString(),
                        gender.getText().toString(),
                        age.getText().toString(),
                        maritalStatus.getText().toString(),
                        education.getText().toString(),
                        ethnicGroup.getText().toString(),
                        regularWages.getText().toString(),
                        dailyWages.getText().toString(),
                        nonwagedEarnings.getText().toString(),
                        seasonalEarnings.getText().toString(),
                        sourceOfIncome.getText().toString(),
                        mainIncome.getText().toString(),
                        secondIncome.getText().toString(),
                        fullTimeMale.getText().toString(),
                        fullTimeFemale.getText().toString(),
                        partTimeMale.getText().toString(),
                        partTimeFemale.getText().toString(),
                        totalHouseHold.getText().toString(),
                        governmentService.getText().toString(),
                        privateSector.getText().toString(),
                        services.getText().toString(),
                        trade.getText().toString(),
                        construction.getText().toString(),
                        agriculture.getText().toString(),
                        casuallabor.getText().toString(),
                        totalother.getText().toString(),
                        eaarned.getText().toString(),
                        govPension.getText().toString(),
                        govAssistance.getText().toString(),
                        remittances.getText().toString(),
                        rentalIncome.getText().toString(),
                        nonearnedOthers.getText().toString(),
                        source.getText().toString(),
                        vegetables.getText().toString(),
                        rice.getText().toString(),
                        otherCrop.getText().toString(),
                        livestock.getText().toString(),
                        poultry.getText().toString(),
                        forestProducts.getText().toString(),
                        handicrafts.getText().toString(),
                        estimateOthers.getText().toString(),
                        house.getText().toString(),
                        roof.getText().toString(),
                        walls.getText().toString(),
                        floor.getText().toString(),
                        disabledMale.getText().toString(),
                        disabledFemale.getText().toString(),
                        disabledillness.getText().toString(),
                        fmailyLiving.getText().toString(),
                        maleArrayList,
                        femaleArrayList
                        );

                        //step 2
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",tempHouseholdDate);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }



        } );
        maleList = (RecyclerView) findViewById(R.id.maleList);
        maleAddapter = new MaleAddapter(this, maleArrayList, this);
        final LinearLayoutManager Manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        maleList.setLayoutManager(Manager);
        maleList.setAdapter(maleAddapter);
        TextView maletext = (TextView) findViewById(R.id.maletext);
        maletext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaleDialog(-1);
            }
        });


        femaleList = (RecyclerView) findViewById(R.id.femaleList);
        femaleAddapter = new FemaleAddapter(this,femaleArrayList, this);
        final LinearLayoutManager Managerfemale = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        femaleList.setLayoutManager(Managerfemale);
        femaleList.setAdapter(femaleAddapter);
        TextView addfemale = (TextView) findViewById(R.id.femaletext);
        addfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFemaleDialog(-1);
            }
        });



        try {
            HouseholdDate householdDate = (HouseholdDate) getIntent().getSerializableExtra("data");
            if (householdDate != null) {

                householdHead.setText(householdDate.householdHead);
                gender.setText(householdDate.gender);
                age.setText(householdDate.age);
                maritalStatus.setText(householdDate.maritalStatus);
                education.setText(householdDate.education);
                ethnicGroup.setText(householdDate.ethnicGroup);
                regularWages.setText(householdDate.regularWages);
                dailyWages.setText(householdDate.dailyWages);
                nonwagedEarnings.setText(householdDate.nonwagedEarnings);
                seasonalEarnings.setText(householdDate.seasonalEarnings);
                sourceOfIncome.setText(householdDate.sourceOfIncome);
                mainIncome.setText(householdDate.mainIncome);
                secondIncome.setText(householdDate.secondIncome);
                fullTimeMale.setText(householdDate.fullTimeMale);
                fullTimeFemale.setText(householdDate.fullTimeFemale);
                partTimeMale.setText(householdDate.partTimeMale);
                partTimeFemale.setText(householdDate.partTimeFemale);
                totalHouseHold.setText(householdDate.totalHouseHold);
                governmentService.setText(householdDate.governmentService);
                privateSector.setText(householdDate.privateSector);
                services.setText(householdDate.services);
                trade.setText(householdDate.trade);
                construction.setText(householdDate.construction);
                agriculture.setText(householdDate.agriculture);
                casuallabor.setText(householdDate.casuallabor);
                totalother.setText(householdDate.totalother);
                eaarned.setText(householdDate.eaarned);
                govPension.setText(householdDate.govPension);
                govAssistance.setText(householdDate.govAssistance);
                remittances.setText(householdDate.remittances);
                rentalIncome.setText(householdDate.rentalIncome);
                nonearnedOthers.setText(householdDate.nonearnedOthers);
                source.setText(householdDate.source);
                vegetables.setText(householdDate.vegetables);
                rice.setText(householdDate.rice);
                otherCrop.setText(householdDate.otherCrop);
                livestock.setText(householdDate.livestock);
                poultry.setText(householdDate.poultry);
                forestProducts.setText(householdDate.forestProducts);
                handicrafts.setText(householdDate.handicrafts);
                estimateOthers.setText(householdDate.estimateOthers);
                house.setText(householdDate.house);
                roof.setText(householdDate.roof);
                walls.setText(householdDate.walls);
                floor.setText(householdDate.floor);
                disabledMale.setText(householdDate.disabledMale);
                disabledFemale.setText(householdDate.disabledFemale);
                disabledillness.setText(householdDate.disabledillness);
                fmailyLiving.setText(householdDate.fmailyLiving);



            }
        } catch (Exception e) {
            Log.e("xxxxxx", "Something went wrong");
        }

    }
    public void showMaleDialog(final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OneHousehold.this);
        LayoutInflater inflater = OneHousehold.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.male, null);

        final TextView submit = (TextView) dialogView.findViewById(R.id.submit);
        final EditText maleFirstYear = (EditText) dialogView.findViewById(R.id.maleFirstYear);
        final EditText maleSecondYear = (EditText) dialogView.findViewById(R.id.maleSecondYear);
        final EditText maleThirdYear = (EditText) dialogView.findViewById(R.id.maleThirdYear);
        final EditText maleFourYear = (EditText) dialogView.findViewById(R.id.maleFourYear);
        final EditText maleFiveYear = (EditText) dialogView.findViewById(R.id.maleFiveYear);
        final EditText maleSixYear = (EditText) dialogView.findViewById(R.id.maleSixYear);

        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();


        if (position != -1) {
            submit.setText("UPDATE");
            Male bean = maleArrayList.get(position);
            maleFirstYear.setText(bean.maleFirstYear);
            maleSecondYear.setText(bean.maleSecondYear);
            maleThirdYear.setText(bean.maleThirdYear);
            maleFourYear.setText(bean.maleFourYear);
            maleFiveYear.setText(bean.maleFiveYear);
            maleSixYear.setText(bean.maleSixYear);

        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == -1) {
                    Male bean = new Male(
                            maleFirstYear.getText().toString(),
                            maleSecondYear.getText().toString(),
                            maleThirdYear.getText().toString(),
                            maleFourYear.getText().toString(),
                            maleFiveYear.getText().toString(),
                            maleSixYear.getText().toString()








                            );
                    maleArrayList.add(bean);
                } else {
                    maleArrayList.get(position).setMaleFirstYear(maleFirstYear.getText().toString());
                    maleArrayList.get(position).setMaleSecondYear(maleSecondYear.getText().toString());
                    maleArrayList.get(position).setMaleThirdYear(maleThirdYear.getText().toString());
                    maleArrayList.get(position).setMaleFourYear(maleFourYear.getText().toString());
                    maleArrayList.get(position).setMaleFiveYear(maleFiveYear.getText().toString());
                    maleArrayList.get(position).setMaleSixYear(maleSixYear.getText().toString());


                }
                maleAddapter.notifyData(maleArrayList);
                b.cancel();
            }
        });
        b.show();
    }
    public void showFemaleDialog(final int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OneHousehold.this);
        LayoutInflater inflater = OneHousehold.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.female, null);

        final TextView submit = (TextView) dialogView.findViewById(R.id.submit);
        final EditText femaleFirstYear = (EditText) dialogView.findViewById(R.id.femaleFirstYear);
        final EditText femaleSecondYear = (EditText) dialogView.findViewById(R.id.femaleSecondYear);
        final EditText femaleThirdYear = (EditText) dialogView.findViewById(R.id.femaleThirdYear);
        final EditText femaleFourYear = (EditText) dialogView.findViewById(R.id.femaleFourYear);
        final EditText femaleFiveYear = (EditText) dialogView.findViewById(R.id.femaleFiveYear);
        final EditText femaleSixYear = (EditText) dialogView.findViewById(R.id.femaleSixYear);



        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();


        if (position != -1) {
            submit.setText("UPDATE");
            Female bean = femaleArrayList.get(position);
            femaleFirstYear.setText(bean.femaleFirstYear);
            femaleSecondYear.setText(bean.femaleSecondYear);
            femaleThirdYear.setText(bean.femaleThirdYear);
            femaleFourYear.setText(bean.femaleFourYear);
            femaleFiveYear.setText(bean.femaleFiveYear);
            femaleSixYear.setText(bean.femaleSixYear);



        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == -1) {
                    Female bean = new Female(
                            femaleFirstYear.getText().toString(),
                            femaleSecondYear.getText().toString(),
                            femaleThirdYear.getText().toString(),
                            femaleFourYear.getText().toString(),
                            femaleFiveYear.getText().toString(),
                            femaleSixYear.getText().toString()

                            );
                    femaleArrayList.add(bean);
                } else {
                    femaleArrayList.get(position).setFemaleFirstYear(femaleFirstYear.getText().toString());
                    femaleArrayList.get(position).setFemaleSecondYear(femaleSecondYear.getText().toString());
                    femaleArrayList.get(position).setFemaleThirdYear(femaleThirdYear.getText().toString());
                    femaleArrayList.get(position).setFemaleFourYear(femaleFourYear.getText().toString());
                    femaleArrayList.get(position).setFemaleFiveYear(femaleFiveYear.getText().toString());
                    femaleArrayList.get(position).setFemaleSixYear(femaleSixYear.getText().toString());

                }
                femaleAddapter.notifyData(femaleArrayList);
                b.cancel();
            }
        });
        b.show();
    }

    @Override
    public void itemMaleClick(int position) {

    }

    @Override
    public void itemFemaleClick(int position) {

    }
}