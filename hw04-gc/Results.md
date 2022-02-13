# До оптимизации

- Heap 256Mb  - `java.lang.OutOfMemoryError: Java heap space`
- Heap 512Mb  - `spend msec:10608, sec:10`
- Heap 1024Mb - `spend msec:9754, sec:9`
- Heap 2048Mb - `spend msec:9250, sec:9`
- Heap 3060Mb - `spend msec:9230, sec:9`
- Heap 4096Mb - `spend msec:9364, sec:9`
- Heap 8192Mb - `spend msec:9921, sec:9`

# После оптимизации
- Heap 32Mb   - `java.lang.OutOfMemoryError: Java heap space`
- Heap 64Mb   - `spend msec:1562, sec:1`
- Heap 128Mb  - `spend msec:1575, sec:1`
- Heap 256Mb  - `spend msec:1431, sec:1`
- Heap 512Mb  - `spend msec:1439, sec:1`
- Heap 1024Mb - `spend msec:1436, sec:1`
- Heap 2048Mb - `spend msec:1455, sec:1`

 **Оптимальный размер хипа: 256 Mb**
