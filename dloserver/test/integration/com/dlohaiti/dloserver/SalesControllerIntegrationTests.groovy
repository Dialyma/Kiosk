package com.dlohaiti.dloserver

import com.dlohaiti.dloserver.endpoint.SalesController
import org.junit.Before
import org.junit.Test

class SalesControllerIntegrationTests extends GroovyTestCase {

    SalesController controller = new SalesController()

    @Before
    void setup() {
        new Kiosk(name: "k1").save()
    }

    @Test
    void shouldSaveValidDataToDb() {
        controller.request.json = '{"kiosk":"k1", "timestamp":"2013-04-24 12:00:01 EDT","quantity":1,"sku":"10GAL"}'

        controller.save()

        assert '{"msg":"OK"}' == controller.response.contentAsString
        assert 201 == controller.response.status
        assert 1 == Sale.count()

        Sale savedSale = Sale.first()
        assert savedSale.kiosk == new Kiosk(name: "k1")
        assert savedSale.quantity == 1
        assert savedSale.sku == "10GAL"
    }

    @Test
    void shouldRejectInvalidTimestampFormat() {
        controller.request.json = '{"kiosk":"k1", "timestamp":1000932563,"quantity":1,"sku":"10GAL"}'

        controller.save()

        assert 422 == controller.response.status
        assert 0 == Sale.count()
    }

    @Test
    void shouldRejectMissingKiosk() {
        controller.request.json = '{"kiosk":"k2", "timestamp":"2013-04-24 12:00:01 EDT","quantity":1,"sku":"10GAL"}'

        controller.save()

        assert 422 == controller.response.status
        assert 0 == Sale.count()
    }
}