./gradlew clean build fatjar bundle
rm -rf /home/aneline/Desktop/chess/chess.jar
rm -rf /home/aneline/Desktop/chess/chess.sh
cp build/bundle/chess.jar /home/aneline/Desktop/chess/chess.jar
cp build/bundle/chess.sh /home/aneline/Desktop/chess/chess.sh
chmod +x /home/aneline/Desktop/chess/chess.sh
/home/aneline/Dev/uci/Arena_x86_64_linux
sleep 5000
