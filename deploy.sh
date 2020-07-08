#!/bin/bash

./gradlew clean buildUI build -x test && java -ea -Dspring.profiles.active=dev -jar build/libs/battleship-game.jar
