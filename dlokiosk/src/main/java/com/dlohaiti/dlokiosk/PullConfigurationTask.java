package com.dlohaiti.dlokiosk;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.dlohaiti.dlokiosk.client.*;
import com.dlohaiti.dlokiosk.db.ProductRepository;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.dlohaiti.dlokiosk.db.SamplingSiteParametersRepository;
import com.dlohaiti.dlokiosk.domain.*;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PullConfigurationTask extends RoboAsyncTask<String> {
    private static final String TAG = PullConfigurationTask.class.getSimpleName();
    private ProgressDialog dialog;
    @Inject private ConfigurationClient client;
    @Inject private ProductRepository productRepository;
    @Inject private PromotionRepository promotionRepository;
    @Inject private SamplingSiteParametersRepository samplingSiteParametersRepository;
    @Inject private KioskDate kioskDate;
    @InjectResource(R.string.fetch_configuration_failed) private String fetchConfigurationFailedMessage;
    @InjectResource(R.string.fetch_configuration_succeeded) private String fetchConfigurationSucceededMessage;
    private Context context;

    public PullConfigurationTask(Context context) {
        super(context);
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override protected void onPreExecute() throws Exception {
        dialog.setMessage("Loading Configuration From Server...");
        dialog.show();
    }

    @Override public String call() throws Exception {
        Configuration c = client.fetch();
        List<Product> products = new ArrayList<Product>();
        for(ProductJson p : c.getProducts()) {
            Money price = new Money(p.getPrice().getAmount());
            products.add(new Product(null, p.getSku(), null, p.isRequiresQuantity(), 1, p.getMinimumQuantity(), p.getMaximumQuantity(), price, p.getDescription(), p.getGallons()));
        }
        List<Promotion> promotions = new ArrayList<Promotion>();
        for(PromotionJson p : c.getPromotions()) {
            PromotionApplicationType appliesTo = PromotionApplicationType.valueOf(p.getAppliesTo());
            Date start = kioskDate.getFormat().parse(p.getStartDate());
            Date end = kioskDate.getFormat().parse(p.getEndDate());
            promotions.add(new Promotion(null, p.getSku(), appliesTo, p.getProductSku(), start, end, p.getAmount().toString(), PromotionType.valueOf(p.getType()), null));
        }

        List<ParameterSamplingSites> samplingSiteParameters = new ArrayList<ParameterSamplingSites>();
        for(ParameterJson p : c.getParameters()) {
            Parameter parameter = new Parameter(p.getName(), p.getUnit(), p.getMinimum(), p.getMaximum(), p.isOkNotOk());
            List<SamplingSite> samplingSites = new ArrayList<SamplingSite>();
            for(SamplingSiteJson site : p.getSamplingSites()) {
                samplingSites.add(new SamplingSite(site.getName()));
            }
            samplingSiteParameters.add(new ParameterSamplingSites(parameter, samplingSites));
        }
        if(productRepository.replaceAll(products)) {
            Log.i(TAG, "products successfully replaced");
        }
        if(promotionRepository.replaceAll(promotions)) {
            Log.i(TAG, "promotions successfully replaced");
        }
        if(samplingSiteParametersRepository.replaceAll(samplingSiteParameters)) {
            Log.i(TAG, "sampling sites and parameters successfully updated");
        }
        return "";
    }

    @Override protected void onSuccess(String s) throws Exception {
        Toast.makeText(context, fetchConfigurationSucceededMessage, Toast.LENGTH_LONG).show();
    }

    @Override protected void onException(Exception e) throws RuntimeException {
        Log.e(TAG, "Error fetching configuration from server", e);
        Toast.makeText(context, fetchConfigurationFailedMessage, Toast.LENGTH_LONG).show();
    }

    @Override protected void onFinally() throws RuntimeException {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
