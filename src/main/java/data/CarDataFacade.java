package data;

import config.Configuration;
import data.db.Car;
import data.db.low.CarItem;
import data.dealer.DealerMdt;
import data.offer.OfferMdt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz on 07.02.15.
 */
public class CarDataFacade {

    private DealerMdt dealerMdt = new DealerMdt();
    private OfferMdt offerMdt = new OfferMdt();
    private Integer pagesTotalCount = 10; // some place to start ;(

    public List<Car> getPremiumCars() throws Configuration.ConfigurationException {
        List<Car> cars = new ArrayList<Car>();
        try {
            CarItem[] premiumCars = offerMdt.getPremiumCars();
            for (int i = 0; i < premiumCars.length; i++) {
                Car car = CarTransformer.transform(premiumCars[i]);
                car.setDealer(dealerMdt.getPremiumCarDealer(car.getId()));
                cars.add(car);
            }
        } catch (OfferMdt.OfferMdtException e) {
            e.printStackTrace();
        } catch (DealerMdt.DealerMdtException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> getCars(int page) throws Configuration.ConfigurationException {
        List<Car> result = new ArrayList<Car>();
        try {
            String mainStringFromPage = offerMdt.getMainStringFromPage(page);
            CarItem[] cars = offerMdt.getCarsFromMainString(mainStringFromPage);
            pagesTotalCount = Integer.valueOf(offerMdt.getPagesTotalCountFromMainString(mainStringFromPage));
            for (int i = 0; i < cars.length; i++) {
                Car car = CarTransformer.transform(cars[i]);
                car.setDealer(dealerMdt.getCarDealer(car.getId()));
                result.add(car);
            }
        } catch (OfferMdt.OfferMdtException e) {
            e.printStackTrace();
        } catch (DealerMdt.DealerMdtException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Integer getPagesTotalCount() {
        return pagesTotalCount;
    }
}
