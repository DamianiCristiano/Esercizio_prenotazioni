package classiMie;
import java.util.Arrays;

public class DynamicArray {

	private Object[] arr;
	
	
	public DynamicArray() {
        this.arr = new Object[0];
    }
	
	public int size() {
		return this.arr.length;
	}
	
	public void add(Object obj) {
		int size = this.size();
		this.arr = Arrays.copyOf(this.arr, size + 1);
		this.arr[size] = obj;
	}
	
	
	public Object get(int i) {
        return this.arr[i];
    }
	
	public void set(int i, Object obj) {
		this.arr[i] = obj;;
	}
	
	public void remove(int p) {
		int size = this.size();
		Object[] arrTemp = this.arr;
		this.arr = new Object[size - 1];
		for (int j = 0, i = 0; j < arrTemp.length ; j++) {
			if(j != p) {
				this.arr[i] = arrTemp[j];
				i++;
			}
        }
	}
	
	public void printArray() {
		for (int i = 0; i < this.arr.length; i++) {
            System.out.println((i+1) + ") " + this.arr[i] + " ");
        }
    }
	
}
