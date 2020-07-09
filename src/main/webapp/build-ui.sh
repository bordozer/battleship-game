#!/bin/bash

npm run build --display-error-details

cp -R build/dist/* ../resources/static
