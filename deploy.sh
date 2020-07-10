#!/bin/bash

./gradlew clean build -x test && java -ea -Dspring.profiles.active=dev -jar build/libs/battleship-game.jar
#./gradlew clean buildUI build -x check
#./gradlew clean buildUI build -x check && java -ea -Dspring.profiles.active=dev -jar build/libs/battleship-game.jar
