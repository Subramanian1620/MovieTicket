import java.util.logging.Logger;
import java.util.logging.Level;
import com.zc.component.object.ZCObject; 
import com.zc.component.object.ZCRowObject; 
import com.zc.component.object.ZCTable;
import com.zc.component.zcql.ZCQL;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.catalyst.advanced.CatalystAdvancedIOHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import org.json.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zc.component.ZCUserDetail; 
import com.zc.component.users.ZCUser;
import com.zc.component.*;

public class MovieAction implements CatalystAdvancedIOHandler {
	private static final Logger LOGGER = Logger.getLogger(MovieAction.class.getName());
	private static String baseURL = "https://angular-60013485476.development.catalystserverless.in";
	
	@Override
    public void runner(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			switch(request.getRequestURI()) {
				case "/": {
					String name = (String) request.getParameter("name");
					LOGGER.log(Level.INFO, "Hello "+name);
					response.setStatus(200);
					response.getWriter().write("Hello from MovieAction.java");
					break;	
				}
				case "/validate": {
					ZCUserDetail details = ZCUser.getInstance().getCurrentUser();
					//System.out.println(details);
					details.getRoleDetails();
					ZCRoleDetails roleDetails=details.getRoleDetails();
					//System.out.println(roleDetails.getName());
					// if(roleDetails.getName()!=null){
					response.setStatus(200);
					response.getWriter().write(String.valueOf(roleDetails.getName().equals("App Administrator")));
				// }
				// else{
				// 	System.out.println("---------------");
				// 	response.getWriter().write("login");
				// 	response.setStatus(200);
				// }
					break;	
				}
				case "/createmovie" : {
					//Create a base Object Instance 
					ZCObject object = ZCObject.getInstance(); 
					//Get a Table Instance referring the tableID on base object 
					ZCTable tab = object.getTable("1141000000028818"); 
					//Create a row instance 
					ZCRowObject row = ZCRowObject.getInstance(); 
					//Set the required column values using set() method on the row instance 
					row.set("moviename",(String) request.getParameter("moviename"));
					
					// System.out.println(request.getPart("uploadpuc").getSize());
					//Add the single row to table by calling insertRow() method 
					tab.insertRow(row);
					response.setStatus(200);
					// response.getWriter().write("Added Successfully");
					break;
				}
				case "/addtheatres" : {
					//Create a base Object Instance 
					ZCObject object = ZCObject.getInstance(); 
					//Get a Table Instance referring the tableID on base object 
					ZCTable tab = object.getTable("1141000000030594"); 
					//Create a row instance 
					ZCRowObject row = ZCRowObject.getInstance(); 
					//Set the required column values using set() method on the row instance 
					row.set("theatresname",(String) request.getParameter("theatresname"));
					row.set("location",(String) request.getParameter("location"));
					row.set("totalseats",Integer.valueOf(request.getParameter("totalseats")));
					//Add the single row to table by calling insertRow() method 
					// ZCUserDetail details = ZCUser.getInstance().getCurrentUser();
					// System.out.println(details.getUserId());
					// System.out.println(details);
					tab.insertRow(row);
					response.setStatus(200);
					// response.getWriter().write("Added Successfully");
					break;
				}
				case "/addshowtimings" : {
					//Create a base Object Instance 
					ZCObject object = ZCObject.getInstance(); 
					//Get a Table Instance referring the tableID on base object 
					ZCTable tab = object.getTable("1141000000031336"); 
					//Create a row instance 
					ZCRowObject row = ZCRowObject.getInstance(); 
					//Set the required column values using set() method on the row instance 
					row.set("theatresid",(String) request.getParameter("theatresid"));
					row.set("movieid",(String) request.getParameter("movieid"));
					String datestring=request.getParameter("showtime");
					row.set("showtime",datestring);
					tab.insertRow(row);
					response.setStatus(200);
					// response.getWriter().write("Added Successfully");
					break;
				}
				case "/getmovie" : {
					ArrayList<HashMap> movieList=new ArrayList<HashMap>();
					ZCQL.getInstance().executeQuery(String.format("select ROWID,moviename from movie")).forEach(row->{
						movieList.add(new HashMap(){
							{
							put("id",row.get("movie","ROWID").toString());
							put("moviename",row.get("movie","moviename").toString());
							}
						});
					});
					JSONArray jsArray = new JSONArray();
					ObjectMapper objectMapper = new ObjectMapper();
					for(int i=0;i<movieList.size();i++){
						// jsArray.put(objectMapper.writeValueAsString(movieList.get(i)));
						jsArray.put(new JSONObject(movieList.get(i)));
					}
					response.setStatus(200);
					response.getWriter().write(jsArray.toString());
					break;
				}
				case "/gettheatres" : {
					ArrayList<HashMap> movieList1=new ArrayList<HashMap>();
					ZCQL.getInstance().executeQuery(String.format("select ROWID,theatresname from theatres")).forEach(row->{
						movieList1.add(new HashMap(){
							{
							put("id",row.get("theatres","ROWID").toString());
							put("theatresname",row.get("theatres","theatresname").toString());
							} 
						});
					});
					JSONArray jsArray = new JSONArray();
					ObjectMapper objectMapper = new ObjectMapper();
					for(int i=0;i<movieList1.size();i++){
						// jsArray.put(objectMapper.writeValueAsString(movieList.get(i)));
						jsArray.put(new JSONObject(movieList1.get(i)));
					}
					System.out.println(jsArray);
					response.setStatus(200);
					response.getWriter().write(jsArray.toString());
					break;
				}
				case "/getshowtimes" : { 
					ArrayList<HashMap> movieList=new ArrayList<HashMap>();
					ZCQL.getInstance().executeQuery(String.format("select showtimings.*,movie.*,theatres.* from showtimings INNER JOIN movie ON showtimings.movieid=movie.ROWID INNER JOIN theatres ON showtimings.theatresid=theatres.ROWID order by showtimings.theatresid, showtimings.showtime")).forEach(row->{
						movieList.add(new HashMap(){
							{
							put("showtimeid",row.get("showtimings","ROWID").toString());
							put("showtime",row.get("showtimings","showtime").toString());
							put("moviename",row.get("movie","moviename").toString());
							put("theatresname",row.get("theatres","theatresname").toString());
							put("totalseats",row.get("theatres","totalseats"));
							}
						});
					});
					HashMap showidvsbookedseats =new HashMap();
					ZCQL.getInstance().executeQuery(String.format("select COUNT(ROWID),sum(noofseats),bookedtickets.showid from bookedtickets GROUP BY showid")).forEach(row->{
						showidvsbookedseats.put(row.get("bookedtickets","showid").toString(),row.get("bookedtickets","noofseats"));
					});
					// System.out.println(showidvsbookedseats);
					int availableSeats=0;
					for (int i = 0; i < movieList.size(); i++){
						if(showidvsbookedseats.containsKey((String)movieList.get(i).get("showtimeid"))){
							availableSeats=Integer.valueOf((String)movieList.get(i).get("totalseats"))  -  Integer.valueOf((String)showidvsbookedseats.get((String)movieList.get(i).get("showtimeid")));
							//int availableSeats=Integer.valueOf(movieList.get(i).get("totalseats").toString())-Integer.valueOf(showidvsbookedseats.get(movieList.get(i).get("showtimeid").toString()));
               				// System.out.print(movieList.get(i) + " "+availableSeats); 
						}
						else
						{
							availableSeats=Integer.valueOf((String)movieList.get(i).get("totalseats"));
						}
						movieList.get(i).put("availableSeats",availableSeats);
					}
					JSONArray jsArray = new JSONArray();
					// ObjectMapper objectMapper = new ObjectMapper();
					for(int i=0;i<movieList.size();i++){
						// jsArray.put(objectMapper.writeValueAsString(movieList.get(i)));
						jsArray.put(new JSONObject(movieList.get(i)));
					}
					System.out.println(jsArray);
					response.setStatus(200);
					response.getWriter().write(jsArray.toString());
					break;
				}
				case "/booktickets" : { 
					HashMap showidvsbookedseats =new HashMap();
							//ZCObject object = ZCObject.getInstance(); 
							ZCQL.getInstance().executeQuery(String.format("select COUNT(ROWID),sum(noofseats),bookedtickets.showid from bookedtickets where showid="+request.getParameter("showid")+" GROUP BY showid")).forEach(row->{
						showidvsbookedseats.put(row.get("bookedtickets","showid").toString(),row.get("bookedtickets","noofseats"));
					});
					HashMap showidvstotalseats =new HashMap();
							ZCObject object = ZCObject.getInstance(); 
							ZCQL.getInstance().executeQuery(String.format("select showtimings.ROWID,theatres.totalseats from showtimings INNER JOIN theatres ON showtimings.theatresid=theatres.ROWID where ROWID="+request.getParameter("showid"))).forEach(row->{
						showidvstotalseats.put(row.get("showtimings","ROWID").toString(),row.get("theatres","totalseats"));
					});
					int availableSeats = 0;
					// System.out.println(showidvstotalseats);
					// System.out.println(Integer.valueOf(showidvstotalseats.get(request.getParameter("showid")).toString()));
					if(showidvsbookedseats.containsKey(request.getParameter("showid"))){
					availableSeats = Integer.parseInt(showidvstotalseats.get(request.getParameter("showid")).toString()) - Integer.parseInt(showidvsbookedseats.get(request.getParameter("showid")).toString());
					}
					else
					availableSeats=Integer.parseInt(showidvstotalseats.get(request.getParameter("showid")).toString());
					// System.out.println(availableSeats);
					if(Integer.valueOf((String) request.getParameter("noofseats"))<=availableSeats)
					{
						// response.getWriter().write("Added Successfully");
						ZCTable tab = object.getTable("1141000000032810"); 
							ZCRowObject row = ZCRowObject.getInstance(); 
							ZCUserDetail details = ZCUser.getInstance().getCurrentUser();
							// System.out.println(details.getUserId().toString());
							row.set("userid",(String)details.getUserId().toString());
							row.set("showid",(String) request.getParameter("showid"));
							row.set("noofseats",(String) request.getParameter("noofseats"));
							row.set("amount",(String) request.getParameter("amount"));
							tab.insertRow(row);
							
							// response.getWriter().write("Added Successfully");
					}
					else
					response.getWriter().write("failed");
					response.setStatus(200);	
					break;
				}
				case "/viewTicket" : {
					HashMap viewticket =new HashMap();
					ArrayList<HashMap<String, Object>> movieList = new ArrayList<>();
							//ZCObject object = ZCObject.getInstance(); 
							//ZCRowObject row = ZCRowObject.getInstance(); 
							ZCUserDetail details = ZCUser.getInstance().getCurrentUser();
							// System.out.println(details.getUserId().toString());
							ZCQL.getInstance().executeQuery(String.format("SELECT showtimings.*, bookedtickets.*, movie.*, theatres.* FROM bookedtickets INNER JOIN showtimings ON bookedtickets.showid = showtimings.ROWID INNER JOIN movie ON showtimings.movieid = movie.ROWID INNER JOIN theatres ON showtimings.theatresid = theatres.ROWID WHERE bookedtickets.userid=" + details.getUserId().toString())).forEach(row -> {
						movieList.add(new HashMap(){
							{
							put("showtime",row.get("showtimings","showtime").toString());
							put("moviename",row.get("movie","moviename").toString());
							put("theatresname",row.get("theatres","theatresname").toString());
							put("noofseats",row.get("bookedtickets","noofseats"));
							}
						});
						
					});
					JSONArray jsArray = new JSONArray();
					// ObjectMapper objectMapper = new ObjectMapper();
					for(int i=0;i<movieList.size();i++){
						// jsArray.put(objectMapper.writeValueAsString(movieList.get(i)));
						jsArray.put(new JSONObject(movieList.get(i)));
					}
					System.out.println(jsArray);
					response.getWriter().write(jsArray.toString());
					response.setStatus(200);	
					break;
				}
				case "/adminshows" : { 
					ArrayList<HashMap> movieList=new ArrayList<HashMap>();
					ZCQL.getInstance().executeQuery(String.format("select showtimings.*,movie.*,theatres.* ,bookedtickets.*, from showtimings INNER JOIN movie ON showtimings.movieid=movie.ROWID INNER JOIN theatres ON showtimings.theatresid=theatres.ROWID order by showtimings.theatresid, showtimings.showtime")).forEach(row->{
						movieList.add(new HashMap(){
							{
							put("showtimeid",row.get("showtimings","ROWID").toString());
							put("showtime",row.get("showtimings","showtime").toString());
							put("moviename",row.get("movie","moviename").toString());
							put("theatresname",row.get("theatres","theatresname").toString());
							}
						});
					});
					JSONArray jsArray = new JSONArray();
					for(int i=0;i<movieList.size();i++){
						jsArray.put(new JSONObject(movieList.get(i)));
					}
					System.out.println(jsArray);
					response.setStatus(200);
					response.getWriter().write(jsArray.toString());
					break;
				}
				case "/deleteshowtimes" : { 
					HashMap showidvsamount =new HashMap();
					HashMap showidvsbookedseats =new HashMap();
							ZCQL.getInstance().executeQuery(String.format("select sum(amount), sum(noofseats),bookedtickets.showid from bookedtickets GROUP BY showid")).forEach(row->{
						showidvsamount.put(row.get("bookedtickets","showid").toString(),row.get("bookedtickets","amount"));
						showidvsbookedseats.put(row.get("bookedtickets","showid").toString(),row.get("bookedtickets","noofseats"));
					});
					ArrayList<HashMap> movieList=new ArrayList<HashMap>();

					ZCQL.getInstance().executeQuery(String.format("select showtimings.*,movie.*,theatres.* from showtimings INNER JOIN movie ON showtimings.movieid=movie.ROWID INNER JOIN theatres ON showtimings.theatresid=theatres.ROWID order by showtimings.theatresid, showtimings.showtime")).forEach(row->{
						movieList.add(new HashMap(){
							{
							put("showtimeid",row.get("showtimings","ROWID").toString());
							put("showtime",row.get("showtimings","showtime").toString());
							put("moviename",row.get("movie","moviename").toString());
							put("theatresname",row.get("theatres","theatresname").toString());
							put("totalseats",row.get("theatres","totalseats").toString());
							if(showidvsamount.containsKey(row.get("showtimings","ROWID").toString())){
							put("amount",showidvsamount.get(row.get("showtimings","ROWID").toString()).toString());
							put("bookedseats",showidvsbookedseats.get(row.get("showtimings","ROWID").toString()).toString());
							put("seatsfree",(Integer.valueOf(row.get("theatres","totalseats").toString())-Integer.valueOf(showidvsbookedseats.get(row.get("showtimings","ROWID")).toString())));
							}
							else{
							put("amount","0");
							put("bookedseats","0");
							put("seatsfree",row.get("theatres","totalseats").toString());
							}
							}
						});
					});
					
					JSONArray jsArray = new JSONArray();
					for(int i=0;i<movieList.size();i++){
						jsArray.put(new JSONObject(movieList.get(i)));
					}
					System.out.println(jsArray);
					response.setStatus(200);
					response.getWriter().write(jsArray.toString());
					break;
				}
				case "/cancelshows" : { 
					ArrayList<HashMap> movieList=new ArrayList<HashMap>();
					ZCQL.getInstance().executeQuery(String.format("delete from showtimings where ROWID="+request.getParameter("showid")));
					
					response.setStatus(200);
					// response.getWriter().write("Deleted Successfully");
					break;
				}
				default: {
					response.setStatus(404);
					response.getWriter().write("You might find the page you are looking for at \"/\" path");
				}
			}
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Exception in MovieAction",e);
			response.setStatus(500);
			response.getWriter().write("Internal server error");
		}
	}
	
}