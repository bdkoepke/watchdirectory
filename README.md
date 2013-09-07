Observable wrapper around the java WatchService mechanism introduced in java 7.
Makes it much easier to watch directories in another thread and handle the 
events. Includes a sample application which can recursively or non-recursively
watch directories and invoke processes when new filesystem events occur. 
The application is configurable via xml.
