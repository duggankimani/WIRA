package com.duggan.workflow.test.Animal;

public class Animal {
		private String name;
		private int weight;
		private String sound;
		
		public Flys flyingType;
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setWeight(int weight) {
			if(weight > 0){
			this.weight = weight;
			}else{
				System.out.println("Weight should be greater than 0");
			}
		}
		
		public int getWeight() {
			return weight;
		}
		
		public void setSound(String sound) {
			this.sound = sound;
		}
		
		public String getSound() {
			return sound;
		}
		
		public String DoesItFly(){
			return flyingType.Fly();
		}
		
		public void setFlyType(Flys newFlyType){
			this.flyingType=newFlyType;
		}
		
		public static void main(String[] args) {
			String name="gigi.bpmn";
			int idx = name.lastIndexOf('.');
			String xtension= name.substring(idx+1, name.length());
			System.out.println(xtension);
		}
}
