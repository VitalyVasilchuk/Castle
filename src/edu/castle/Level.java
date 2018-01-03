package edu.castle;

public class Level {

    private int number;	    // номер уровня
    private String sLevel;  // строка, описывающая уровень
    private int cols;	    // макс количество ячеек по горизонтали (колонки)
    private int rows;	    // макс количество ячеек по вертикали (строки)
    
    private final String[] levels = {
	"",
	
	  "####################" + "\n"
	+ "# M  $ #          G#" + "\n"
	+ "###H###########H####" + "\n"
	+ "# GH     M     H   #" + "\n"
	+ "###########H########" + "\n"
	+ "# $        H G     #" + "\n"
	+ "###############H####" + "\n"
	+ "# W    2       H  G#" + "\n"
	+ "##H#################" + "\n"
	+ "# H               G#" + "\n"
	+ "#######H#########H##" + "\n"
	+ "#      H    $#   HG#" + "\n"
	+ "####H######-##H#####" + "\n"
	+ "#$  H  D #    H 1 G#" + "\n"
	+ "####################",
	
	  "#######################" + "\n"
	+ "#G  D B#$     $#R D  G#" + "\n"
	+ "## ##H###-#H#-###H## ##" + "\n"
	+ "#S $ H 2   H    1H $ A#" + "\n"
	+ "##H# ###H#####H### #H##" + "\n"
	+ "# HW    H  #  H    MH #" + "\n"
	+ "#######################",
	
	  "####################" + "\n"
	+ "#D   G# B R  #W   M#" + "\n"
	+ "###H### #H#-####H###" + "\n"
	+ "#$ H  # 2H1   MMMMM#" + "\n"
	+ "####################"
    };

    public Level() {
	this(1);
    }

    public Level(int number) {
	this.number = number;
	sLevel = levels[number];
	
        int i = sLevel.indexOf('\n');
	cols = i;
        while (i >= 0) {
            rows++;
            i = sLevel.indexOf('\n', i + 1);
        }
	rows++;
    }

    public char[][] getLevelData() {
	int x = 0;
	int y = 0;
	char[][] aLevel = new char[rows][cols];

	for (int i = 0; i < sLevel.length(); i++) {
	    char item = sLevel.charAt(i);
	    switch (item) {
		case '\n':
		    x = 0;
		    y++;
		    break;
		default:
		    aLevel[y][x] = item;
		    x++;
		    break;
	    }

	}
	
	return aLevel;
    }

    public int getCols() {
	return cols;
    }

    public int getRows() {
	return rows;
    }

    @Override
    public String toString() {
	return "Level{" + "number=" + number +  ", cols=" + cols + ", rows=" + rows + ", sLevel=\n" + sLevel + '}';
    }

}
