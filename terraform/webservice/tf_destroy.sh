#!/usr/bin/env bash

SERVICE_NAME="battleship-game"

ENV=$1
if [ -z "$ENV" ]
then
      echo "ENV is empty. Provide 'dev' or 'prod'"
      exit 1;
fi

echo "=================================================="
echo "Environment '${ENV}' is going to be destroyed!"
echo "=================================================="

CONFIRM_STR="Destroy ${SERVICE_NAME}-${ENV}"

read -r -p "Type '${CONFIRM_STR}' to proceed: " confirm
if [ "${confirm}" = "${CONFIRM_STR}" ]; then
   terraform destroy "-var-file=env/${ENV}.tfvars" -auto-approve
   echo "Environment '${ENV}' has been destroyed. R.I.P."
   exit 0
fi

echo ""
echo "Wrong confirmation"
echo ""
