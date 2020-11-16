import { readFileSync, writeFileSync } from 'fs-extra';
import { parse, stringify } from 'yaml'

/**
 * @author Til Blechschmidt
 */

const securityPath = 'security.json';
const specificationPath = 'target/api.yml';

const rawSecurity = readFileSync(securityPath, { encoding: 'utf-8' });
const rawSpec = readFileSync(specificationPath, { encoding: 'utf-8' });

const security = JSON.parse(rawSecurity);
let spec = parse(rawSpec);

spec.components.securitySchemes = security.schemes;
spec.security = [
    { [security.defaultScheme]: [] }
]

for (const operationToOverride in security.overrides) {
    if (!security.overrides.hasOwnProperty(operationToOverride)) continue;
    for (const path of spec.paths) {
        for (const operation of path) {
            if (operation.operationId === operationToOverride) {
                operation.security = security.overrides[operationToOverride];
            }
        }
    }
}

const processedSpec = stringify(spec);
writeFileSync(specificationPath, processedSpec, { encoding: 'utf-8'});
