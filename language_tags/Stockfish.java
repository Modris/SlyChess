package com.modris.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Stockfish {
	private static Logger logger = LoggerFactory.getLogger(Stockfish.class);

	private static final Object processLock = new Object();

	private int strength;
	private final ProcessBuilder processBuilder;
	private Process process = null;
	private BufferedReader reader = null;
	private OutputStreamWriter writer = null;

	public Stockfish(String engineLocation) {
		this.strength = 1600; // default elo...
		this.processBuilder = new ProcessBuilder(engineLocation);
	}
	
	public int getStrength() {
		return strength;
	}


	public void start() {
		logger.info("Trying to start Stockfish Engine launch process.");

		try {
			this.process = processBuilder.start();
			this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			this.writer = new OutputStreamWriter(process.getOutputStream());
		} catch (IOException error) {
			logger.error("Exception while starting Stockfish engine:" + error);
		}
	}

	public String findBestMove(String fen) {
		synchronized (processLock) {
			if (this.process != null && this.process.isAlive()) {
			//	logger.info("Finding the best move...");

				try {

					writer.flush();
					writer.write("ucinewgame\n");
					writer.flush();

					// setting default values...
					// ucinewgame will reset the option values which is why these commands are not
					// at the start() method.
					writer.write("setoption name UCI_LimitStrength value true\n");
					writer.flush();
					writer.write("setoption name UCI_Elo value " + this.strength + "\n");
					writer.flush();

					String fenPosition = "position fen " + fen + "\n";
					writer.write(fenPosition);
					writer.flush();

					writer.write("go movetime 100\n"); // 200ms movetime.
					writer.flush();

					String line = "";
					while ((line = reader.readLine()) != null) {
						if (line.contains("bestmove")) {
							String cleanedLine = line.substring(9, 13);
							// "bestmove c7a7" to "c7a7"
							// "bestmove c3h8 ponder d8h4" to "c3h8"
							return cleanedLine;
						}
					}
				} catch (IOException error) {
					logger.error("Error in finding the best move. Writer/Reader: " + error);
				}
			} else {
				logger.error("Can't find the best move if Stockfish engine is not running! Something went wrong. Restarting engine.");
				close();
				start();
				return "";
			}
			return "";
		}
	}

	public void close() {
		if (this.process != null && this.process.isAlive()) {
			this.process.destroy();
			logger.info("Closing Stockfish engine...");
		}
		try {
		     if (reader != null) {
		            reader.close();
		    		logger.info("Closing Reader");
		        }
		        if (writer != null) {
		            writer.close();
		        	logger.info("Closing Writer IO...");
		        }
		} catch (IOException error) {
			logger.error("Exception closing reader or writer: " + error);
		}
	}

	public void setStrength(int strength) {
		if (strength < 1320) {
			this.strength = 1320;
		} else if (strength > 3190) {
			this.strength = 3190;
		} else {
			this.strength = strength;
		}
	
	}

}
