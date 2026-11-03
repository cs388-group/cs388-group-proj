# cs388-group-proj

Political news can be heavily influenced by the perspectives and biases of the media outlets. If people had a simple way to access proposed bills, legislative updates, and exactly who voted for what, misleading narratives would become much harder to sustain. This app aims to put the information directly in people's hands so they can research the facts and draw their own conclusions regardless of the lies politicians assert.
___ Description ___
____addname here___ is a mobile application that helps people stay informed about political decisions and legislation happening in their communities. The app uses a user's location to identify relevant local, state, and federal representatives and legislation.
Users can personalize their political feed by selecting topics they care about, such as education, healthcare, housing, taxes, transportation, and the environment. Instead of searching through complicated government websites, users can see bills they find interesting, understand what they propose in simple language, and follow their progress.
The app also takes advantage of mobile features such as location services, and push notifications. For example, users can receive an alert when a bill related to one of their interests is introduced, voted on, or passed.
The overall goal is to make government information easier to access and understand while encouraging people to become more involved in their local communities.

__ App Eval ___
•	Category: Government / Politics / News. The app focuses on making political information more accessible to everyday citizens, particularly people interested in local and regional government.

•	Mobile: 
The app is created specifically for mobile devices rather than being a website. The app uses GPS/location services to identify relevant political districts, interactive maps to explore representatives and legislation, and push notifications to alert users about important political updates. Users can also save bills and share summaries directly from their phones.
•	Story: 
Many people want to understand what their government is doing but do not know where to find reliable information. Government websites can be difficult to navigate, and legislation is often written in complicated language. CivicPulse solves this problem by bringing relevant political information directly to users and explaining it in a way that is easier to understand.
•	Market: 
The target audience includes students, young adults, voters, and anyone involved in government decisions that affect their communities.
•	Habit: 
Users can regularly check their personalized political feed for new legislation and updates. Push notifications encourage users to return when bills they follow change status. Users can also create watchlists, save bills, and share political updates with friends and classmates.
•	Scope: 
The initial version will focus on location selection, retrieving legislation from a government API, displaying bills in a feed, and allowing users to select political interests. 



____ Required Features ____
1.	User Location Selection
*Allow users to select state(s) or enter ZIP code(s) of locations they are interested in the politics of or just allow the app to use the user's location.
*Use their location to determine which political region and legislation are relevant.
*Allow users to change their location manually.
2.	Personalized Political Interests
*Allow users to select topics they care about; examples: data centers, education, healthcare, housing, taxes, transportation, environment, construction.
*	Use selected interests to prioritize relevant bills in their feed.
3.	Political stream
*Display a list of proposed bills and legislation.
*Show the bill title, description, location, and current status.
*Allow users to browse and scroll through relevant political updates.
4.	Bill Details
*	Allow users to select a bill to view more info.
*	Display its description, current legislative status, and relevant dates.
*	Include a link to the original government source.
*	Names of legislators who passed and rejected the bill
  
5.	^^Maybe^^ Bill Search engine
*	Allow users to search for specific bills
*	Filter bills by topic, location, and legislative status
  
___ Screen Archetypes ___
_Home Screen (Political Feed)
* Display legislation relevant to the user's location and selected interests.
*	Show recently introduced bills and important legislative updates.
*	Allow users to select a bill to view additional information.
  
_Explore Screen (Search and Discovery)^^maybe^^
*	Search for legislation by name or keyword.
*	Filter legislation by topic, state, and status.
*	Discover bills outside the user's personalized feed.
  
_Bill Details Screen
*	Display the full details of a selected bill.
*	Show a simplified explanation and current legislative status.
*	Allow users to save or share a bill.
*	Link to the official government source.

_ Alert Screen
*	Display notifications about legislation users follow.
*	Show updates when bills are introduced, voted on, or passed.
*	Allow users to open the associated bill.
  
_Profile / Settings Screen
*	Allow users to update their location.
*	Manage selected political interests.
*	Manage notification preferences.
*	View saved bills and watchlists.


____ Navigation ____
Tab Navigation (Tab to Screen)
* Home → Personalized political feed.
* Explore → Search and discover legislation.
* Alerts → View political notifications.
* Profile → Manage account, interests, location, and saved bills.
  
_Flow Navigation (Screen-to-Screen)

_Home Screen
*Select a bill → Bill Details Screen.
*	Select an update notification → Bill Details Screen.
*	Navigate to Explore, Alerts, or Profile using the bottom navigation bar.
*	Explore Screen
*	Search for a bill → Search Results.
*	Select a bill → Bill Details Screen.
*	Apply filters → Filtered Results.

_Alerts Screen
*	Select an alert → Bill Details Screen.
*	Profile Screen
*	Edit location → Location Selection.
*	Edit interests → Interest Selection.
*	Manage notifications → Notification Settings.
* Select saved bill → Bill Details Screen.


