import java.io.DataInputStream;
import java.io.FileInputStream;

public class IpsFileResolver {
	public static void main(String[] args) throws Exception {
		DataInputStream dis = new DataInputStream(new FileInputStream("test.ips"));
		System.out.println(getString(dis, 5));
		String content;
		int offset, length, rleSize, value;
		while (true) {
			offset = getValue(dis, 3);
			if (offset == 0x454f46)//EOF
				break;

			length = getValue(dis, 2);
			if (length == 0) {// IPS RLE
				rleSize = getValue(dis, 2);
				value = getValue(dis, 1);
				content = getContent(value, rleSize);
			} else {
				content = getContent(dis, length);
			}
			System.out.println("Offset:" + hex(offset) + "\tlength:" + length + "\tcontent:" + content);
		}

		dis.close();
	}

	public static int getValue(DataInputStream dis, int length) throws Exception {
		int rtn = dis.readUnsignedByte();
		for (int i = 1; i < length; i++) {
			rtn <<= 8;
			rtn += dis.readUnsignedByte();
		}
		return rtn;
	}

	public static String getString(DataInputStream dis, int length) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
			sb.append(String.format("%c", dis.readUnsignedByte()));
		return sb.toString();
	}

	public static String getContent(DataInputStream dis, int length) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
			sb.append(String.format("%02x ", dis.readUnsignedByte()));
		return sb.toString();
	}

	public static String getContent(int value, int length) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
			sb.append(String.format("%02x ", value));
		return sb.toString();
	}

	public static String hex(int i) {
		return String.format("0x%x", i);
	}
}
