package socket;

import java.util.Arrays;

/**
 * ����ɾ������Ԫ�ص��߼�
 * @author ta
 *
 */
public class Demo {
	public static void main(String[] args) {
		int[] allOut = {1,2,3,4,5,6,7,8,9};	
		int pw = 3;
		//��pw��������ɾ��
		for(int i=0;i<allOut.length;i++) {
			if(allOut[i]==pw) {
				//�����һ��Ԫ�ط�������
				allOut[i] = allOut[allOut.length-1];
				//����
				allOut = Arrays.copyOf(
						allOut, allOut.length-1);
				break;
			}
		}
		
		
		
		System.out.println(Arrays.toString(allOut));
	}
}



