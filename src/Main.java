import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.stream.Stream;

import com.sun.xml.internal.bind.CycleRecoverable.Context;

import sun.awt.CausedFocusEvent.Cause;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;;

public class Main {

	public static void main(String[] args) throws IOException {
		Path dirPath = Paths.get("income");
		
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
		dirPath.register(watchService, ENTRY_CREATE,ENTRY_MODIFY,ENTRY_DELETE);
	

	for(;;) {
		WatchKey key;
		try {
			key = watchService.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		for (WatchEvent<?> event : key.pollEvents()) {
			if (event.kind() == ENTRY_CREATE) {
				Path filePath = ((WatchEvent<Path>) event).context().toAbsolutePath();
				System.out.println("New file =" + filePath);
				System.out.println("Processing...");
				try {
					Stream<String> stream = Files.lines(filePath);
					stream.forEach(System.out::println);
					stream.close();
				} catch (Throwable e) {
					System.out.println();
				}
				
				
			} else if (event.kind() == ENTRY_MODIFY) {
				System.out.println("File was modify " + ((WatchEvent<Path>) event).context().toAbsolutePath());
			} else if (event.kind() == ENTRY_DELETE) {
				System.out.println("File was delete " + ((WatchEvent<Path>) event).context().toAbsolutePath());
			}
		}
		
		if (!key.reset()) {
			break;
		}
	}
		
	}
}
