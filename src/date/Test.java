package date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * ��дһ������Ҫ���û������Լ������գ���ʽ
 * Ϊ:yyyy-MM-dd
 * Ȼ�󾭹��������㣬���������Ϊֹһ������
 * ������.
 * ����������10000��ļ����������죬�����ʽ
 * ͬ��Ϊ:yyyy-MM-dd
 * @author ta
 *
 */
public class Test {
	public static void main(String[] args) throws ParseException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("��������������:(yyyy-mm-dd)");
		String dateStr = scanner.nextLine();
		SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd"	
		);
		Date birth = sdf.parse(dateStr);
		Date now = new Date();
		long time = now.getTime()-birth.getTime();
		time = time/1000/60/60/24;
		System.out.println("��ϲ�����Ѿ�����"+time+"�죬���������!");
		time = birth.getTime()+1000L*60*60*24*10000;
		Date date = new Date(time);
		String line = sdf.format(date);
		System.out.println("������10000��ļ�����Ϊ:"+line);
		
		
	}
}









