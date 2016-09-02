import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BlackJack extends JFrame implements ActionListener
{
	private File f = new File("./src/data/");
	private ArrayList<String> shoe = new ArrayList<String>();
	private ArrayList<String> hand = new ArrayList<String>();
	private ArrayList<String> dealer = new ArrayList<String>();
	private ArrayList<String> deck = new ArrayList<String>();
	private ArrayList<JLabel> icons = new ArrayList<JLabel>();
	private ArrayList<JLabel> deckIcons = new ArrayList<JLabel>();
	private ArrayList<JLabel> chips = new ArrayList<JLabel>();
	private String hiddenCard;
	private JFrame frame = new JFrame();
	private JLabel icon, message, playerScore, dealScore, bet, playerMoney, clock, chip;
	private JButton hit, stand, deal, doub, surrender, increase, decrease, help;
	private SecureRandom r = new SecureRandom();
	private int seed = 0;
	private int[] handCoord = {200,300};
	private int[] dealerCoord = {200,100};
	private int yourScore, dealerScore, money, moneyBet, chipY;
	private int turn;
	private BufferedImage img;
	private MyClock myClock;
	private boolean blackJack = false;

	public BlackJack()
	{
		chipY = 250;
		for(int i = 0; i < 8; i++)
		{
			shoe.addAll(Arrays.asList(f.list()));
		}
		money = 2000;
		moneyBet = 0;
		// Building Frame
		frame.setResizable(false);
		frame.setTitle("BlackJack Game");
		frame.setLayout(null);
		frame.setBounds(0,0,650,600);
		frame.setContentPane(new JLabel(new ImageIcon("./src/table.jpg")));
		frame.setLocationRelativeTo(null);
		//Labels	
		playerScore = new JLabel("Your Score: ");
		playerScore.setFont(new Font("Arial",1 , 17));
		playerScore.setForeground(Color.BLACK);
		playerScore.setBounds(200, 400, 150, 22);
		playerScore.setVisible(true);
		frame.add(playerScore);
		
		dealScore = new JLabel("Dealer Score: ");
		dealScore.setFont(new Font("Arial",1 , 17));
		dealScore.setForeground(Color.BLACK);
		dealScore.setBounds(200, 50, 150, 22);
		dealScore.setVisible(true);
		frame.add(dealScore);
		
		message = new JLabel("");
		message.setFont(new Font("Arial", 1 , 20));
		message.setForeground(Color.BLACK);
		message.setBounds(220, 250, 200, 20);
		message.setVisible(false);
		frame.add(message);
		
		bet = new JLabel("Bet: " + moneyBet);
		bet.setBounds(500, 420, 95, 20);
		bet.setFont(new Font("Arial",1 , 14));
		bet.setForeground(Color.BLACK);
		frame.add(bet);
		
		playerMoney = new JLabel("Your money: " + money);
		playerMoney.setBounds(500, 390, 200, 20);
		playerMoney.setFont(new Font("Arial",1 , 14));
		playerMoney.setForeground(Color.BLACK);
		frame.add(playerMoney);
		
		clock = new JLabel();
		clock.setBounds(20, 15, 80, 20);
		clock.setFont(new Font("Arial",1,13));
		clock.setForeground(Color.BLACK);
		frame.add(clock);
		myClock = new MyClock(clock);
		
		//Buttons
		deal = new JButton("Deal");
		deal.setBounds(50,430,80,20);
		frame.add(deal);
		deal.setEnabled(false);
		deal.addActionListener(this);
		
		hit = new JButton("Hit");
		hit.setBounds(135,430,80,20);
		frame.add(hit);
		hit.setEnabled(false);
		hit.addActionListener(this);
		
		stand = new JButton("Stand");
		stand.setBounds(305, 430, 80, 20);
		frame.add(stand);
		stand.setEnabled(false);
		stand.addActionListener(this);
		
		doub = new JButton("Double");
		doub.setBounds(220, 430, 80, 20);
		frame.add(doub);
		doub.setEnabled(false);
		doub.addActionListener(this);
		
		increase = new JButton("+");
		increase.setBounds(500, 450, 45, 20);
		frame.add(increase);
		increase.addActionListener(this);
		
		decrease = new JButton("-");
		decrease.setBounds(550, 450, 45, 20);
		frame.add(decrease);
		decrease.setEnabled(false);
		decrease.addActionListener(this);
		
		surrender = new JButton("Surrender");
		surrender.setBounds(390, 430, 95, 20);
		frame.add(surrender);
		surrender.setEnabled(false);
		surrender.addActionListener(this);
		
		help = new JButton("Help");
		help.setBounds(50, 50, 70, 20);
		frame.add(help);
		help.addActionListener(this);
		
		drawBacks();
			       
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args)
	{
	//	System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		new BlackJack();
	}
	private void drawBacks()
	{
		int x = 500;
		int y = 100;
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./src/back/Backface_Blue.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < 30; i++)
		{
			icon = new JLabel();
			ImageIcon image = new ImageIcon(img);
			icon.setIcon(image);
			icon.setFocusable(false);
			icon.setBounds(x, y, 50, 70);
			deckIcons.add(icon);
			x += 2;
			y -= 3;
		}
		for(int i = deckIcons.size()-1 ; i > -1; i-- )
		{
			frame.add(deckIcons.get(i));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// First Deal of cards
		if (e.getSource() == deal)
		{
			resetGUI();
			
			handCoord = drawCard(handCoord,false);
			handCoord = drawCard(handCoord,false);
			turn = 1;
			dealerCoord = drawCard(dealerCoord,false);
			dealerCoord = drawCard(dealerCoord,true);
			turn = 0;
			deal.setEnabled(false);
			stand.setEnabled(true);
			hit.setEnabled(true);
			doub.setEnabled(true);
			increase.setEnabled(false);
			decrease.setEnabled(false);
			surrender.setEnabled(true);
			if(money < moneyBet)
				doub.setEnabled(false);
			if (yourScore == 21)
			{
				checkForBlackJack(hand, 0);
				if(blackJack)
					reset();
			}
			if (blackJack)
			{
				reset();
			}
		}
		//Getting new card to player's hand
		if(e.getSource() == hit)
		{
			handCoord = drawCard(handCoord,false);
			surrender.setEnabled(false);
			if(yourScore > 21)
				compareScore(blackJack);
		}
		if(e.getSource() == doub)
		{
			money -= moneyBet;
			moneyBet += moneyBet;
			hit.setEnabled(false);
			stand.setEnabled(false);
			doub.setEnabled(false);
			surrender.setEnabled(false);
			handCoord = drawCard(handCoord,false);
			turn = 1;
			openHiddenCard();
			checkScore(1, dealer);
			if (dealerScore < 17 && yourScore < 22)
			{
				dealerDraws();
			}
			compareScore(blackJack);
		}
		if(e.getSource() == stand)
		{
			hit.setEnabled(false);
			stand.setEnabled(false);
			doub.setEnabled(false);
			surrender.setEnabled(false);
			turn = 1;
			openHiddenCard();
			checkScore(1, dealer);
			if (dealerScore < 17)
			{
				dealerDraws();
			}
			compareScore(blackJack);
		}
		if (e.getSource() == surrender)
		{
			message.setVisible(true);
			message.setText("You Lost " + moneyBet/2);
			money += moneyBet/2;
			moneyBet = 0;
			bet.setText("Bet: " + moneyBet);
			playerMoney.setText("Your money: " + money);
			hit.setEnabled(false);
			stand.setEnabled(false);
			doub.setEnabled(false);
			surrender.setEnabled(false);
			reset();
		}
		if (e.getSource() == increase)
		{
			if (moneyBet < 300)
			{
				money -= 50;
				moneyBet += 50;
				bet.setText("Bet: " + moneyBet);
				playerMoney.setText("Your money: " + money);
				decrease.setEnabled(true);
				if(moneyBet == 300)
				{
					increase.setEnabled(false);
				}	
				if (moneyBet == 50)
				{
					deal.setEnabled(true);
				}
				if (money == 0)
					increase.setEnabled(false);
				drawChips();
				message.setVisible(false);
			}				
		}
		if (e.getSource() == decrease)
		{
			if (moneyBet > 0)
			{
				moneyBet -= 50;
				money += 50;
				bet.setText("Bet: " + moneyBet);
				playerMoney.setText("Your money: " + money);
				increase.setEnabled(true);
				if(moneyBet == 0)
				{
					decrease.setEnabled(false);
					deal.setEnabled(false);
				}
				removeChips();
			}
		}
		if (e.getSource() == help)
		{
			JOptionPane.showMessageDialog(null, "Objective:\n"
					+ "          The objective of the game is to beat the dealer. If your cards total higher than the dealer's cards without\n"
					+ "going over 21 you win. You are not trying to get close to 21. If your hand or the dealer's hand goes over 21\n"
					+ "you Bust. If you bust you automatically lose. If the dealer busts and you do not, you win. The player must\n"
					+ "act first. If the player busts he loses regardless if the dealer busts or not.\n\n"
					+ "Card Values:\n"
					+ "          The suits of the cards have no affect on the game. Cards 2 - 10 are counted at face value without regards to\n"
					+ "their suit. All face cards have a value of ten. An ace can count as either one or eleven.\n\n"
					+ "Blackjack:\n"
					+ "          If you or the dealer is dealt an Ace and a ten-value card you have 21 known as a blackjack. This is a natural.\n"
					+ "If you get the blackjack you will be paid 3 to 2 for your bet providing the dealer does not get one at the same\n"
					+ "time. If you and the dealer have blackjack it is a push. If only the dealer has blackjack all players will lose.\n\n"
					+ "Hitting:\n"
					+ "          To take a hit means that you want to draw another card. To signal the dealer for a hit you will tap the table\n"
					+ "in front of you or make a beckoning motion with your hand. If you wish another card after the first you would\n"
					+ "motion in the same manner.\n\n"
					+ "Standing:\n"
					+ "          Once you are satisfied with either your fist two cards or after hitting, you signal the dealer that you wish\n"
					+ "to stand. This is done by waving your hand over the top of your cards.\n\n"
					+ "Doubling Down:\n"
					+ "          When you double down you are allowed to double your bet after receiving your fist two cards. You then receive\n"
					+ "one card only on your hand.\n");
		}
	}
	private void resetGUI()
	{
		message.setVisible(false);
		for(int i = 0; i < icons.size(); i++)
			frame.remove(icons.get(i));
		for(int i = icons.size()-1; i > -1; i--)
			icons.remove(i);
	}
	private void drawChips()
	{
		if(moneyBet != 0)
		{
			int remainder = moneyBet % 20;
			chip = new JLabel();
			if(remainder == 10)
			{
				try 
				{
					img = ImageIO.read(new File("./src/50.png"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else if(remainder == 0)
			{

				frame.remove(chips.get(chips.size()-1));
				chips.remove(chips.size()-1);
				chipY += 5;
				try 
				{
					img = ImageIO.read(new File("./src/100.png"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			ImageIcon image = new ImageIcon(img);
			chip.setIcon(image);
			chip.setBounds(250, chipY, 44, 32);
			chips.add(chip);
			for(int i = chips.size()-1; i > -1; i--)
			{
				frame.add(chips.get(i));
			}
			frame.repaint();
			chipY -= 5;
		}
	}
	private void removeChips()
	{
		frame.remove(chips.get(chips.size()-1));
		chips.remove(chips.size()-1);
		chipY += 5;
		drawChips();
		frame.repaint();
	}
	private int[] drawCard(int[] pos, boolean hidden)
	{
		try
		{
			seed = r.nextInt(shoe.size()-1);
			icon = new JLabel();
			if (!hidden)
			{
				img = ImageIO.read(new File("./src/data/" + shoe.get(seed)));
			}
			else
			{
				img = ImageIO.read(new File("./src/back/Backface_Blue.bmp"));
			}
			ImageIcon image = new ImageIcon(img);
			icon.setIcon(image);
			icon.setBounds(pos[0], pos[1], 50, 70);
			icons.add((JLabel) frame.add(icon));
	        frame.repaint();
	        removeCardFromShoe();
	        if (turn == 0)
	        {
		        hand.add(shoe.get(seed));
		        shoe.remove(seed);
		        checkScore(0,hand);
	        }
	        else
	        {
	        	if(!hidden)
	        	{
	        		dealer.add(shoe.get(seed));
	        	}
	        	else
	        	{       		
	        		hiddenCard = shoe.get(seed);
	        	}
		        shoe.remove(seed);
		        checkScore(1,dealer);
	        }   
		}
		catch(Exception ex)
		{
			
		}
		pos[0] += 55;
		if(yourScore > 21)
		{
			stand.setEnabled(false);
			hit.setEnabled(false);
			doub.setEnabled(false);
		}
		return pos;
	}
	private void dealerDraws()
	{
		do
		{
			dealerCoord = drawCard(dealerCoord, false);
		}
		while(dealerScore < 17);
	}
	private void checkForBlackJack(ArrayList<String> hand, int turn)
	{
		if(hand.get(0).startsWith("10")&& hand.get(1).startsWith("A") || hand.get(0).startsWith("A")&& hand.get(1).startsWith("10"))
		{
			message.setVisible(true);
			blackJack = true;
			
			if(turn == 1)
			{
				message.setText("You Lost " + moneyBet);
			}
			else
			{
				message.setText("You Win " + (moneyBet*2 + moneyBet/2));
				money += moneyBet*2.5;
			}
			moneyBet = 0;
		}
	}
	private void openHiddenCard()
	{
		frame.remove(icons.get(3));
		icons.remove(3);
		icon = new JLabel();
		try {
			img = ImageIO.read(new File("./src/data/" + hiddenCard));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		ImageIcon image = new ImageIcon(img);
		icon.setIcon(image);
		icon.setBounds(255, 100, 50, 70);
		icons.add((JLabel) frame.add(icon));
        frame.repaint();
		dealer.add(hiddenCard);
	}
	private void checkScore(int turn, ArrayList<String> list)
	{
		
		int score = 0;
		
		for(int i = 0; i < list.size(); i++)
		{
			if (list.get(i).startsWith("2"))
				score += 2;
			else if (list.get(i).startsWith("3"))
				score += 3;
			else if (list.get(i).startsWith("4"))
				score += 4;
			else if (list.get(i).startsWith("5"))
				score += 5;
			else if (list.get(i).startsWith("6"))
				score += 6;
			else if (list.get(i).startsWith("7"))
				score += 7;
			else if (list.get(i).startsWith("8"))
				score += 8;
			else if (list.get(i).startsWith("9"))
				score += 9;
			else if (list.get(i).startsWith("A"))
				score += 11;
			else
				score += 10;	
		}
		score = checkForAces(turn, score, list);
		if (turn == 0)
		{
			playerScore.setText("Your Score: " + score);
			yourScore = score;
		}
		else
		{
			dealScore.setText("Dealer Score: " + score);
			dealerScore = score;
			if (dealerScore == 21)
			{
				checkForBlackJack(dealer, 1);
			}
		}
	}
	private int checkForAces(int turn, int score, ArrayList<String> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if (list.get(i).startsWith("A"))
			{
				if((score) > 21)
				{
					score -= 10;
				}
			}
		}
		return score;
	}
	private void compareScore(boolean blackJack)
	{		
		message.setVisible(true);
		if(blackJack == false)
		{
			if (yourScore < 22)
			{
				if(yourScore == 21 && dealerScore != 21)
				{
					message.setText("You Win " + moneyBet*2);
					money += moneyBet*2;
					moneyBet = 0;
				}
				else if (dealerScore > 21)
				{
					message.setText("You Win " + moneyBet*2);
					money += moneyBet*2;
					moneyBet = 0;
				}
				else if (yourScore > dealerScore)
				{
					message.setText("You Win " + moneyBet*2);
					money += moneyBet*2;
					moneyBet = 0;
				}
				else if(yourScore == dealerScore)
				{
					message.setText("Tie");
					money += moneyBet;
					moneyBet = 0;
				}
				else
				{
					message.setText("You Lost " + moneyBet);
					moneyBet = 0;
				}
			}
			else
			{	
					message.setText("You Lost " + moneyBet);
					moneyBet = 0;
			}
		}
		else
		{
			message.setText("You Lost " + moneyBet);
			moneyBet = 0;
		}
		
		bet.setText("Bet: " + moneyBet);
		playerMoney.setText("Your money: " + money);

		reset();
	}
	private void reset()
	{
		if (money > 0)
			increase.setEnabled(true);
		else
			JOptionPane.showMessageDialog(null, "You lost all your money :(");
		yourScore = 0;
		dealerScore = 0;
		handCoord[0] = 200;
		handCoord[1] = 300;
		dealerCoord[0] = 200;
		dealerCoord[1] = 100;
		for (int i = 0; i < hand.size(); i++)
			deck.add(hand.get(i));
		for( int i = hand.size()-1; i > -1; i--)
			hand.remove(i);
		for (int i = 0; i <dealer.size(); i++)
			deck.add(dealer.get(i));
		for(int i = dealer.size()-1; i > -1; i--)
			dealer.remove(i);
		if (shoe.size() < deck.size()/2)
		{
			for(int i = 0; i <deck.size(); i++)
				shoe.add(deck.get(i));
			for(int i = deck.size()-1; i > -1; i--)
				deck.remove(i);
		}
		hit.setEnabled(false);
		stand.setEnabled(false);
		doub.setEnabled(false);
		surrender.setEnabled(false);
		turn = 0;
		blackJack = false;
		chipY = 250;
		resetChips();
	}
	private void resetChips()
	{
		for(int i = 0; i < chips.size(); i++)
			frame.remove(chips.get(i));
		for(int i = chips.size()-1; i > -1; i--)
			chips.remove(i);
	}
	private void removeCardFromShoe()
	{
		if(deckIcons.size() == 0)
			drawBacks();
		frame.remove(deckIcons.get(deckIcons.size()-1));
		deckIcons.remove(deckIcons.size()-1);
	}
}