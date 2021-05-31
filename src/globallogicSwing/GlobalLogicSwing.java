package globallogicSwing;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class GlobalLogicSwing extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public GlobalLogicSwing() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(900, 400));
		setResizable(false);
		
		JPanel p1 = new JPanel();
		p1.add(new JLabel("Enter pattern: "));
		JTextField pattern = new JTextField(10);
		p1.add(pattern);
		p1.add(new JLabel("Enter sentence: "));
		JTextField sentence = new JTextField(40);
		p1.add(sentence);
		
		JPanel p2 = new JPanel();
		TextArea results = new TextArea(15, 100);
		results.setEditable(false);
		//JScrollPane scroll = new JScrollPane(results);
	    //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		p2.add(results);

		
		JPanel p3 = new JPanel();
		JButton ok = new JButton("Calculate");
		p3.add(ok);
		
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(p3, BorderLayout.SOUTH);
		
		ok.addActionListener((listener) -> {
			results.setText("");
			results.setForeground(Color.black);
			if(pattern.getText().isEmpty()) {
				results.append("No pattern given!");
				results.setForeground(Color.red);
			} else if(sentence.getText().isEmpty()) {
				results.append("No sentence given!");
				results.setForeground(Color.red);
			} else {
				String patternString = pattern.getText();
				String sentenceString = sentence.getText();
				
				String[] wordsInSentence = sentenceString.split(" ");
				
				Map<Map<String, Integer>, Integer> resultMap = new HashMap<Map<String,Integer>, Integer>();
				
				int patternCharacters = countPatternCharacters(wordsInSentence, patternString);
				
				int totalCharacters =  countTotalCharacters(wordsInSentence);

				
				if(totalCharacters == 0) {
					results.append("No valid characters in the sentence");
					results.setForeground(Color.red);
				} else {
				
					for(int i = 0; i < wordsInSentence.length; i++) {
						if(wordsInSentence[i].trim().equals("")) continue;
						updateMap(wordsInSentence[i], resultMap, patternString);
					}
					
					
					List<Map.Entry<Map<String, Integer>, Integer>> list = new ArrayList<Map.Entry<Map<String, Integer>, Integer>>(resultMap.entrySet());
					Collections.sort(list, new SwingComparator<Map<String, Integer>, Integer>());
					
					printResult(list, patternCharacters, totalCharacters, results);
				}
			}
		}); 
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			GlobalLogicSwing frame = new GlobalLogicSwing();
			frame.pack();
			frame.setVisible(true);
		});
	}
	
	private static int countTotalCharactersInWord(String word) {
		word = word.trim();
		String invalidCharacters = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		int count = 0;
		for(int i = 0; i < word.length(); i++) {
			if(!invalidCharacters.contains(String.valueOf(word.charAt(i)))) {
				count++;
			}
		}
		
		return count;
	}

	private static int countPatternCharactersInWord(String word, String pattern) {
		int patternCount = 0;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		return patternCount;
	}
		
	private static int countPatternCharacters(String[] wordsInSentence,String pattern) {
		int totalPatternCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			totalPatternCount += countPatternCharactersInWord(wordsInSentence[i], pattern);
		}
		return totalPatternCount;
	}

	private static int countTotalCharacters(String[] wordsInSentence) {
		int totalCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			 if(wordsInSentence[i].trim().equals("")) continue;
			 totalCount += countTotalCharactersInWord(wordsInSentence[i]);
		}
		
		return totalCount;
	}
	
	private static void updateMap(String word, Map<Map<String, Integer>, Integer> resultMap, String pattern) {
		Set<String> set = new LinkedHashSet<String>();
		word = word.toLowerCase();
		int patternCount = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i))) && !set.contains(String.valueOf(word.charAt(i)))) {
				set.add(String.valueOf(word.charAt(i)));
				patternCount++;
			} else if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		final int patternCountFinal = patternCount;
		
		if (set.isEmpty()) {
			return;
		}
		reorderSet(set, pattern);
		
		Map<String, Integer> helpMap = new HashMap<String, Integer>();
		helpMap.put(set.toString(), countTotalCharactersInWord(word));
		
		if(!resultMap.containsKey(helpMap)) {
			resultMap.put(helpMap, patternCount);
		} else {
			resultMap.compute(helpMap, (key, val) -> val + patternCountFinal);
		}
		
		
		
	}

	private static void reorderSet(Set<String> set, String pattern) {
		Set<String> reorderedSet = new LinkedHashSet<String>();
		
		for(int i = 0; i < pattern.length(); i++) {
			if(set.contains(String.valueOf(pattern.charAt(i)))) {
				reorderedSet.add(String.valueOf(pattern.charAt(i)));
				//System.out.println(reorderedSet);
			}
		}
		
		set.removeAll(set);
		set.addAll(reorderedSet);
	}

	private static void printResult(List<Entry<Map<String, Integer>, Integer>> list, int patternCharacters,int totalCharacters, TextArea results) {
			
			for(int i = 0; i < list.size(); i++) {
				String partOfWord = "";
				int lengthOfWord = 0;
				for(Map.Entry<String, Integer> entry : list.get(i).getKey().entrySet()) {
					partOfWord = entry.getKey();
					lengthOfWord = entry.getValue(); 
				}
				results.append("{ (" + partOfWord + "), " + lengthOfWord + "} = " + (double) Math.round(list.get(i).getValue()*1.0/patternCharacters * 100) / 100 +" (" + list.get(i).getValue() + "/" + patternCharacters + ")\n");
			}
			
			results.append("TOTAL Frequency: " + (double) Math.round(patternCharacters*1.0/totalCharacters * 100) / 100 + " (" + patternCharacters + "/" + totalCharacters + ")\n");
		}

}



