package tw.org.sevenflanks.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tw.org.sevenflanks.cli.common.Dispatcher;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@SpringBootApplication
public class CliApplication {

	public static String cd;

	public static void main(String[] args) {
		cd = System.getProperty("user.dir");

		SpringApplication.run(CliApplication.class, args);
	}

	@Autowired
	private Dispatcher dispatcher;

	@PostConstruct
	public void main() {
		Scanner scan = new Scanner(System.in);
		while (scan.hasNext()) {
			final String line = scan.nextLine();
			try {
				if (!dispatcher.go(line)) {
					throw new Exception("wrong executing");
				}
			} catch (Exception e) {
				System.out.println("[ERROR]" + e.getMessage());
			}

		}
	}

}
