#!/bin/sh

set -eu

TZOT_JSON=$(cat ../gen/tzot.json)
cat <<END > check.gen.ts
import {TzotJson} from './tzot_json';

((arg: TzotJson) => {})(${TZOT_JSON})
END

tsc --build --verbose
