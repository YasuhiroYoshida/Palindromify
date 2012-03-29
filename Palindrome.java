package ocjp6;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Palindrome extends HttpServlet {

	public static String contextPath = "";
	public static List<String[]> records; // Players' records arranged by total score
	public static List<String[]> recordsH;// Players' records arranged by highest score
	static {
		records  = new ArrayList<String[]>();
		records.add(new String[] { "", "0", "0", "0" });
		recordsH = new ArrayList<String[]>();
		recordsH.add(new String[] { "", "0", "0", "0" });
	}

	// Home Page Generator
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		// Build output
		contextPath = request.getContextPath();

		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<html><head><title>Palindromify</title></head><body>");

		out.println("<form method=\"post\" action =\"" + contextPath + "/Palindrome\" >");
		out.println("<table border=\"0\">");
		out.println("<tr><td align=\"center\" colspan=\"2\"><font size=\"+2\">Palindromify</font></td></tr>");
		out.println("<tr><td>Name:&nbsp;</td><td><input type=\"text\" name=\"name\" size=\"42\"></td></tr>");
		out.println("<tr><td>Word/Phrase:&nbsp;</td><td><input type=\"text\" name=\"phrase\" size=\"42\"></td></tr>");
		out.println("<tr><td align=\"center\" colspan=\"2\"></br>" +
				"<input type=\"submit\" value=\"Fire\"></td></tr>");
		out.println("<tr><td align=\"center\" colspan=\"2\">" +
				"<input type=\"button\" value=\"Hall of Fame\" onClick=\"location.href=\'"
				+ contextPath + "/HallOfFame.jsp\'\" ></td></tr>");
		out.println("</table>");
		out.println("</form>");
		
		out.println("</body></html>");
	} // end of doGet()

	// Result Page Generator
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		// Initializer
		String name = request.getParameter("name");
		String phrase = request.getParameter("phrase");
		contextPath = request.getContextPath();
		int score = 0;       // Current player's latest score
		int totalScore = 0;  // Current player's total score
		int highestScore = 0;// Current player's highest score

		// Building output (header)
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<html><head><title>Palindromifyちとしはちとしは</title></head><body>");
		out.println("<table border=\"0\">");
		out.println("<tr><td align=\"center\" colspan=\"2\"><font size=\"+2\">Palindromify</font></td></tr>");
		out.println("<tr><td align=\"center\" colspan=\"2\">");
	
		// Evaluating input
		synchronized(this){// in case this app is not deployed over a thread-safe platform
			
			// 1. Check if input "name" and "phrase" exist
			if (!name.isEmpty() && !phrase.isEmpty()) {
	
				// 2. Check if the current player is an existing player
				boolean isOldPlayer  = false;// whether or not the current player is an existing player
				String[] tempRecord = { name, "0", "0", "0" };// the record for the current player
				for (String[] r : records) {
					if (name.equalsIgnoreCase(r[0])) {// name[0]--score[1]--totalScore[2]--highestScore[3]
						tempRecord = r;
						isOldPlayer = true;
						break;
					}
				}
				
				// 3. Check if input "phrase" is a palindrome
				// Trim and flatten it out for the sake of argument
				String phraseCompressed = phrase.replaceAll(
						"[_\\;\\:.,?!\"\'\\-\\s]", "").toLowerCase();
				char[] phraseCharArray = phraseCompressed.toCharArray();

				// Create a deep copy
				List<Character> charListOriginal = new ArrayList<Character>();
				List<Character> charListCopy = new ArrayList<Character>();
	
				for (Character c : phraseCharArray) {
					charListOriginal.add(c);
					charListCopy.add(c);
				}
				// Convert the copy into a reverse one
				Collections.reverse(charListCopy);
				
				// Parse both phrase, one from head, the other from tail to see if
				// they are actually palindromes
				boolean isPalindrome = true;// whether or not the word/phrase in question is a palindrome
				for ( int i = 0; i < charListOriginal.size(); i++ ){
					if ( !charListOriginal.get(i).equals(charListCopy.get(i)) ) {
						isPalindrome = false;
						break;
					}
				}

				// 4. Calculate the scores
				if (isPalindrome) {
	
					score = (int) Math.ceil((double)charListOriginal.size() / 2);
					totalScore = score + Integer.parseInt(tempRecord[2]);
					highestScore = Integer.parseInt(tempRecord[3]);
					if( highestScore < score ) highestScore = score;
					
				} else {
	
					// Make sure the score is 0 when it's not a palindrome
					// No record will be stored in records for a new player
					score = 0;
					totalScore   = Integer.parseInt(tempRecord[2]);
					highestScore = Integer.parseInt(tempRecord[3]);
	
				}

				// 5. Overwrite or insert the current player's score in "records"
				// name[0]--score[1]--totalScore[2]--highestScore[3]
				tempRecord[1] = String.valueOf(score);
				tempRecord[2] = String.valueOf(totalScore);
				tempRecord[3] = String.valueOf(highestScore);
				
				// Add tempRecord to "records" if it's a new player
				if (!isOldPlayer) {
					// Remove the 1st element of "records" if the pseudo element set up by the initializer is still there 
					if(records.get(0)[0].isEmpty()) {
						records.set(0, tempRecord);
					}else{
						records.add(tempRecord);
					}
				}
				
				// 6. Apply quick sort to "records" in order to create lists on "Hall Of Fame"
				// Apply quick sort in light of totalScore
				recordsH = new ArrayList<String[]>(records);
				if(1 < records.size()) quickSort(records, 0, (records.size() - 1), 2);

				// Apply quick sort in light of highestScore
				recordsH = new ArrayList<String[]>(records); 
				if(1 < recordsH.size()) quickSort(recordsH, 0, (recordsH.size() - 1), 3);
		
				// Build output (body)
				out.println("<font size=\"+1\">Result</font></td></tr><tr><td>");
				out.println("<strong>Name</strong> : </td><td>"    + name         + "</td></tr><tr><td>");
				out.println("<strong>Phrase</strong> : </td><td>"  + phrase       + "</td></tr><tr><td>");
				out.println("<strong>Score</strong> : </td><td>"   + score        + "</td></tr><tr><td>");
				out.println("<strong>Total</strong> : </td><td>"   + totalScore   + "</td></tr><tr><td>");
				out.println("<strong>Highest</strong> : </td><td>" + highestScore + "</td></tr><tr><td colspan=\"2\" align=\"center\">");

			} else {
	
				// Build output (body)
				out.println("<font size=\"+1\">Please try again.</font></br>Both name and word/phrase have to be provided.</td></tr><tr><td colspan=\"2\" align=\"center\">");
	
			}
			
		}// end of syncronized

 		// Build output (footer)
		out.println("</br>");
		out.println("<input type=\"button\" value=\"Try Again\" onClick=\"location.href=\'" + contextPath
					+ "/Palindrome\'\" ></td></tr><tr><td colspan=\"2\" align=\"center\">");
		out.println("<input type=\"button\" value=\"Hall of Fame\" onClick=\"location.href=\'" + contextPath
					+ "/HallOfFame.jsp\'\" ></td></tr><tr><td colspan=\"2\" align=\"center\">");
		out.println("</td></tr><td>");
		
		out.println("</body></html>");

	}// end of doPost
	
	 private void quickSort(List<String[]> list, int leftMostIndex, int rightMostIndex, int switcher){
		 int pointerLeft = leftMostIndex;
		 int pointerRight = rightMostIndex; 
		 int medianValue = Integer.parseInt(list.get((pointerLeft + pointerRight)/2)[switcher]);
	 
		 do {
			 while ( Integer.parseInt( list.get(pointerLeft )[switcher] ) > medianValue ) pointerLeft++;
			 while ( Integer.parseInt( list.get(pointerRight)[switcher] ) < medianValue ) pointerRight--; 
			 if ( pointerLeft <= pointerRight) Collections.swap(list, pointerLeft++, pointerRight--); 
		 } while (pointerLeft <= pointerRight);
	 
	 	 if ( leftMostIndex < pointerRight ) quickSort(list, leftMostIndex, pointerRight, switcher);
	 	 if ( pointerLeft < rightMostIndex ) quickSort(list, pointerLeft, rightMostIndex, switcher);
	 }
	
}