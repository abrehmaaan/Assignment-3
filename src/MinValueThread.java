// Thread class to find the minimum value in a chunk of the array
class MinValueThread extends Thread {
private int[] values;
private int startIndex;
private int endIndex;
private int minValue;

public MinValueThread(int[] values, int startIndex, int endIndex) {
this.values = values;
this.startIndex = startIndex;
this.endIndex = endIndex;
}

@Override
public void run() {
minValue = Integer.MAX_VALUE;
for (int i = startIndex; i < endIndex; i++) {
minValue = Math.min(minValue, values[i]);
}
}

public int getMinValue() {
return minValue;
}
}