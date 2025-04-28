package networkmonitor.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NmapScannerImpl {
	public static String scanHostName(String ipAddress) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("nmap", "-O", "-sS", ipAddress);
			processBuilder.redirectErrorStream(true);
			
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Erro ao executar nmap para IP: " + ipAddress);
            }

            return output.toString();
            
		} catch (Exception e) {
			 e.printStackTrace();
	            throw new RuntimeException("Erro ao escanear dispositivo com Nmap: " + e.getMessage());
		}
	}
	
	
	 public static String extractHostname(String nmapOutput) {
	        //get hostname
	        if (nmapOutput.contains("Nmap scan report for")) {
	            int start = nmapOutput.indexOf("Nmap scan report for") + "Nmap scan report for".length();
	            int end = nmapOutput.indexOf("\n", start);
	            return nmapOutput.substring(start, end).trim();
	        }
	        return "Hostname desconhecido";
	    }

	 public static String extractSistemaOperacional(String nmapOutput) {
		    if (nmapOutput.contains("Aggressive OS guesses:")) {
		        int start = nmapOutput.indexOf("Aggressive OS guesses:") + "Aggressive OS guesses:".length();
		        int end = nmapOutput.indexOf("\n", start);
		        return nmapOutput.substring(start, end).trim();
		    }

		    else if (nmapOutput.contains("OS guesses:")) {
		        int start = nmapOutput.indexOf("OS guesses:") + "OS guesses:".length();
		        int end = nmapOutput.indexOf("\n", start);
		        return nmapOutput.substring(start, end).trim();
		    }
		    
		    return "Sistema Operacional desconhecido";
		}

}
