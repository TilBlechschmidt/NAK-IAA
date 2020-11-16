import {api, config} from "@airtasker/spot";

/*
 * @author Til Blechschmidt
 * These are non-doc comments because the code-gen uses doc-comments
 * for other purposes.
 */

import './account/auth';
import './account/activate';
import './account/request';
import './surveys/create';
import './surveys/query';
import './surveys/get';
import './surveys/delete';
import './surveys/update';
import './surveys/close';
import './surveys/responses/create';
import './surveys/responses/get';
import './surveys/responses/update';

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
