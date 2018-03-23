package adder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

public class ButtonListener implements ActionListener {
	
	JTextField display;
	String pad="";
	private String sum;
	boolean plus= false; // this plus checks if the previous action is "+", defalut is not "+"
	

	public ButtonListener(JTextField display, String sum) {
		
		this.display=display;
		this.sum=sum;
		
		// Done Auto-generated constructor stub.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int result=0; // this is the calculator sum 
		
		JButton b = (JButton) e.getSource(); 
		this.pad=b.getText(); //get the user's input 
		System.out.println(this.plus);	
		if(this.display.getText().equals("0") && this.pad.equals("0")){
			//case 1 where a lot of 0 are keyed in, ex:0000 
			this.display.setText("0");
			this.plus=false;
			
		}else if(this.display.getText().equals("0") && !this.pad.equals("0") && !this.pad.equals("+") && !this.pad.equals("C")){
			//case 2 where non-zero input are keyed in ex:5
			this.display.setText(this.pad);
			this.plus=false;
			
		}else if(!this.display.getText().equals("0") && !this.pad.equals("+") && !this.pad.equals("C") && this.plus!=true){
			//case 3 where 0s follows non-zero input in the front. ex:100
			this.display.setText(this.display.getText()+this.pad);
			this.plus=false;
			
		}else if(this.pad.equals("+") &&  this.plus!=true){
			//case 4 where "+" is pressed 
			this.plus=true; // this.plus checker changes status
			int displayedNum= Integer.parseInt(this.display.getText());
			int toAdd = Integer.parseInt(this.sum);
			result=displayedNum+toAdd; //sum 
			this.sum=String.valueOf(result);
			this.display.setText(this.sum);
			
		}else if (this.pad.equals("C")){
			//case 5 where "clear" is pressed 
			this.sum="0";
			this.display.setText(this.sum);
			this.plus=false;
			System.out.println("5");
			
		}
		else if (!this.pad.equals("+") && this.plus==true){
			//case 6 where previous input was "+", but doesn;t need to sum at this point 
			//ex: 10+ (previous) 50 (now)
			this.display.setText(this.pad);
			this.plus=false;
			
		}else if(this.pad.equals("+") &&  this.plus==true){
			
			this.display.setText(this.display.getText());
			this.sum=this.display.getText();
			this.plus= true;
			
		}
		else{
			this.display.setText(this.pad);
			this.plus=false;//change plus status
			
		}
	}
	
	

}
