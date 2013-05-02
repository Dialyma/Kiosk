package com.dlohaiti.dloserver

import com.grailsrocks.functionaltest.APITestCase

class EndpointsTests extends APITestCase {

    void testServerIsUpAndRunning() {
        get("/healthcheck")

        assertStatus 200
        assertContentStrict '{"db":true}'
    }

    void testPostingAValidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"timestamp":"2013-04-24 00:00:01 EDT","measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}
            """ }
        }

        assertStatus 201
        assertContentStrict '{"msg":"OK"}'
    }

    void testPostingAnEmptyReading() {
        post('/reading')

        assertStatus 422
    }

    void testPostingAnInvalidReading() {
        post('/reading') {
            headers['Content-Type'] = 'application/json'
            body { """
                {"measurements":[
                    {"parameter":"PH","location":"BOREHOLE","value":"5"},
                    {"parameter":"COLOR","location":"WTU_EFF","value":"OK"}
                ]}
            """ }
        }

        assertStatus 422
    }
}
