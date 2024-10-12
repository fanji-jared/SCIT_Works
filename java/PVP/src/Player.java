public class Player {
    private String name;
    private int HP;
    private int AttackNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getAttackNum() {
        return AttackNum;
    }

    public void setAttackNum(int attackNum) {
        AttackNum = attackNum;
    }

    public void Attack(Player player){
        System.out.println("【" + this.name + "】 🗡 【" + player.name + "】" + "造成伤害：" + this.AttackNum);
        int hpTemp = player.getHP() - this.AttackNum;
        System.out.println("【" + player.name + "】" + "剩余血量：" + hpTemp);
        player.setHP(hpTemp);
    }
}