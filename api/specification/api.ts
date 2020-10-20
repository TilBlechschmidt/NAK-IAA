import {api, config} from "@airtasker/spot";

import './users/create';
import './surveys/create';
import './surveys/query';
import './surveys/get';
import './surveys/delete';
import './surveys/update';
import './surveys/close';
import './surveys/responses/create';
import './surveys/responses/get';
import './surveys/responses/update';
import './auth';

@api({
    name: "AppointmentPlanner",
    version: "0.1.0"
})
@config({
    paramSerializationStrategy: {
        query: {
            array: "comma"
        }
    }
})
class Api {}
