import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class project2 {

    public static void main(String[] args) throws IOException {
        //taking args from command line to determine which files are read and write
        String inputInitialFile;
        String inputFile;
        String outputFile;
        inputInitialFile = args[0];
        inputFile=args[1];
        outputFile = args[2];

        FileWriter writer = new FileWriter(outputFile);

        //creating Cities Hashmap to adding cities into it
        HashMap<String, City> cities = new HashMap<>();
        try {
            File myObj = new File(inputInitialFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                //taking employees from initial file and create te city,branch,employee structue
                String data = myReader.nextLine();
                String[] line=data.split(", ");
                String city=line[0].strip();
                String district=line[1].strip();
                String name= line[2].strip();
                String category=line[3].strip();
                City c;
                if(cities.containsKey(city)){
                    c=cities.get(city);
                }
                else{
                    c=new City(city);
                    cities.put(city,c);
                }
                c.setBranchInitial(district);
                Branch b=c.getBranchInitial(district);
                b.setEmployee(name,category);
                Employee e=b.getEmployee(name);
                if(category.equals("MANAGER")){
                    b.manager=e;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            //determining number of employees from different categories in each branch
            for(City city: cities.values()){
                for(Branch branch: city.branches.values()){
                    branch.numCourier=branch.numOfCouriers();
                    branch.numCashier=branch.numOfCashiers();
                    branch.numCook=branch.numOfCooks();
                }
            }
            File myObj2 = new File(inputFile);
            Scanner myReader2 = new Scanner(myObj2);
            Branch currBranch=new Branch("");
            while (myReader2.hasNextLine()) {
                //noArrangment boolean value is a tool to fasting the program and prevent unnecessary controls
                boolean noArrangement=false;
                String line=myReader2.nextLine();
                //analysing the line according to its content
                if (line.contains("PERFORMANCE_UPDATE:")){

                    String[] lineArray=line.split(": ");
                    String tuple=lineArray[1];
                    String[] lineArray2=tuple.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    String name= lineArray2[2].strip();
                    String upNum= lineArray2[3].strip();
                    int updateNum=Integer.parseInt(upNum);

                    City c;
                    c=cities.get(city);
                    c.getBranchInitial(district);
                    Branch b=c.getBranchInitial(district);
                    currBranch=b;
                    boolean tf=b.isEmployeeExist(name);
                    //chech whether there is an employee in that branch. If not, write that.
                    if(!tf){
                        writer.write("There is no such employee.\n");
                        noArrangement=true;
                    }
                    //if employee exists, update this branch's monthly and overall bonuses.
                    else{
                        Employee e=b.getEmployee(name);
                        e.proPoint+=updateNum/200;
                        if(updateNum>0){
                            b.monthlyBonus+=updateNum%200;
                            b.overAllBonus+=updateNum%200;
                        }
                        //if an employee is updated by negative number so that he lose the chance of being manager, delete him from arraylist.
                        if(e.proPoint<10&& Objects.equals(e.category, "COOK")){
                            b.nextManager.remove(e);
                        }
                    }
                }
                else if(line.contains("ADD:")){
                    String[] lineArray=line.split(": ");
                    String tuple=lineArray[1];
                    String[] lineArray2=tuple.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    String name= lineArray2[2].strip();
                    String category= lineArray2[3].strip();
                    City c;
                    c=cities.get(city);
                    c.getBranchInitial(district);
                    Branch b=c.getBranchInitial(district);
                    currBranch=b;
                    boolean tf=b.isEmployeeExist(name);
                    //if employee already exists in structure, print that employee exists and cannot added again.
                    if (tf){
                        writer.write("Existing employee cannot be added again.\n");
                        noArrangement=true;
                    }
                    //update the branch's employee numbers from different categories after adding an element
                    else{
                        b.setEmployee(name,category);
                        if(category.equals("COURIER")){
                            b.numCourier+=1;
                        }
                        if(category.equals("CASHIER")){
                            b.numCashier+=1;
                        }
                        if(category.equals("COOK")){
                            b.numCook+=1;
                        }
                    }
                }
                else if(line.contains("LEAVE:")){
                    String[] lineArray=line.split(": ");
                    String tuple=lineArray[1];
                    String[] lineArray2=tuple.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    String name= lineArray2[2].strip();

                    City c;
                    c=cities.get(city);
                    c.getBranchInitial(district);
                    Branch b=c.getBranchInitial(district);
                    currBranch=b;
                    Employee e=b.getEmployee(name);
                    boolean tf=b.isEmployeeExist(name);
                    //if there is no such employee, write it.
                    if(!tf){
                        writer.write("There is no such employee.\n");
                        noArrangement=true;
                    }
                    else{

                        if(e.category.equals("MANAGER")){

                            //if employee wants to leave is manager, check that are there possible succcessor manager candidates.
                            if(b.nextManager.size()!=0){

                                //if a Cook is a candidate, checck the number of cooks to determine whether he can leave or not
                                if(b.numCook>1){
                                    //if all these conditions valid, leave the employye, make the first candidate manager, and delete it from candidates list.
                                    writer.write(e.name+" is leaving from branch: "+b.branchName+".\n");
                                    writer.write(b.nextManager.get(0).name+" is promoted from Cook to Manager.\n");
                                    b.manager=b.nextManager.get(0);
                                    b.manager.category="MANAGER";
                                    b.manager.proPoint-=10;
                                    b.numCook-=1;
                                    b.leave(e);
                                    b.nextManager.remove(0);
                                }


                            }
                            //if manager can't leave and if he has to right taking bonus, give bonus him.
                            else{
                                if (e.proPoint>-5){
                                    b.monthlyBonus+=200;
                                    b.overAllBonus+=200;
                                }
                            }

                        }

                        //if employee wants to leave is cook, courier or cashier, chech whether he can leave and give bonus acording to their promotion points and leaving condition.
                        else{
                            if(!b.canLeave(e) && e.proPoint>-5){
                                b.monthlyBonus+=200;
                                b.overAllBonus+=200;
                            }
                            else if(b.canLeave(e) ) {
                                writer.write(e.name+" is leaving from branch: "+b.branchName+".\n");
                                String s=e.category;
                                if(s.equals("COURIER")){
                                    b.numCourier-=1;
                                }
                                else if(s.equals("COOK")){
                                    b.numCook-=1;


                                }
                                else {
                                    b.numCashier-=1;
                                }

                                //leave him (delete him from this branchs employee)
                                //if leaving employee is cook and he is in candidates list, delete him from candidates.
                                b.nextManager.remove(e);
                                b.leave(e) ;

                            }
                        }
                    }
                }
                else if(line.contains("PRINT_MONTHLY_BONUSES:")){
                    String[] lineArray=line.split(": ");
                    String cityDistrict=lineArray[1];
                    String[] lineArray2=cityDistrict.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    City c;
                    c=cities.get(city);
                    c.getBranchInitial(district);
                    Branch b=c.getBranchInitial(district);
                    writer.write("Total bonuses for the "+b.branchName+" branch this month are: "+b.monthlyBonus+"\n");

                }
                else if(line.contains("PRINT_OVERALL_BONUSES:")){

                    String[] lineArray=line.split(": ");
                    String cityDistrict=lineArray[1];
                    String[] lineArray2=cityDistrict.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    City c;
                    c=cities.get(city);
                    c.getBranchInitial(district);
                    Branch b=c.getBranchInitial(district);
                    writer.write("Total bonuses for the "+b.branchName+" branch are: "+b.overAllBonus+"\n");
                }
                else if(line.contains("PRINT_MANAGER:")){
                    String[] lineArray=line.split(": ");
                    String cityDistrict=lineArray[1];
                    String[] lineArray2=cityDistrict.split(", ");
                    String city=lineArray2[0].strip();
                    String district= lineArray2[1].strip();
                    City c;
                    c=cities.get(city);
                    Branch b=c.getBranchInitial(district);
                    writer.write("Manager of the "+b.branchName+" branch is "+b.manager.name+".\n");

                }

                //make monthly bonus 0 at the start of the month.
                else if(line.contains("January:")||line.contains("February:")||line.contains("March:")||line.contains("April:")||
                        line.contains("May:")||line.contains("June:")||line.contains("July:")||line.contains("August:")||line.contains("September:")||
                        line.contains("October:")||line.contains("November:")||line.contains("December:")) {
                    for(City city : cities.values()){
                        for(Branch branch: city.branches.values()){
                            branch.monthlyBonus=0;
                        }
                    }
                }
                //make these arrangements after add and update if arrangement is necessary
                if(!noArrangement &&(line.contains("ADD:")||line.contains("UPDATE:"))){


                    //creating an arraylist for removal to prevent deletion in iteration
                    ArrayList<Employee> employeesToRemove = new ArrayList<>();

                    //make this control to each employee to guarantee that prevent problems.
                    for (Employee employee: currBranch.employees.values()) {
                        Branch branch=currBranch;

                        //if updated element is a new candidate for successor manager, add it to the branch's nextManager arraylist.
                        if (employee.category.equals("COOK")&&employee.proPoint>=10&& !branch.nextManager.contains(employee)){
                            branch.nextManager.add(employee);
                        }



                        //cashier to cook promotion
                        if(employee.category.equals("CASHIER")&& employee.proPoint>=3 && branch.numCashier>1){

                            writer.write(employee.name+" is promoted from Cashier to Cook.\n");
                            employee.category="COOK";
                            employee.proPoint-=3;
                            branch.numCashier-=1;
                            branch.numCook+=1;

                        }
                        //control that whether manager will be dismissed, if yes, then control the current employee whether he is in candidates list or not. if he in, make him manager and dismiss manager.
                        else if(branch.nextManager.contains(employee)&&branch.numCook>1 && branch.isManagerWillDismiss()){

                            if(employee.equals(branch.nextManager.get(0))){
                                writer.write(branch.manager.name+" is dismissed from branch: "+branch.branchName+".\n");
                                writer.write(employee.name+" is promoted from Cook to Manager.\n");
                                employee.category="MANAGER";
                                employee.proPoint-=10;
                                branch.numCook-=1;
                                Employee temp=branch.manager;
                                employeesToRemove.add(temp);
                                branch.manager=employee;
                                branch.nextManager.remove(0);
                            }

                        }
                        //cashier's dismissing condition
                        else if(employee.category.equals("CASHIER")&& employee.proPoint<=-5 && branch.numCashier>1){

                            writer.write(employee.name+" is dismissed from branch: "+branch.branchName+".\n");
                            employeesToRemove.add(employee);
                            branch.numCashier-=1;

                        }
                        //couries's dismissing condition
                        else if(employee.category.equals("COURIER")&& employee.proPoint<=-5 && branch.numCourier>1){
                            writer.write(employee.name+" is dismissed from branch: "+branch.branchName+".\n");
                            employeesToRemove.add(employee);
                            branch.numCourier-=1;


                        }
                        //cook's dismissing condition
                        else if(employee.category.equals("COOK")&& employee.proPoint<=-5 && branch.numCook>1){
                            writer.write(employee.name+" is dismissed from branch: "+branch.branchName+".\n");
                            employeesToRemove.add(employee);
                            branch.numCook-=1;
                            branch.nextManager.remove(employee);
                        }
                    }

                    //delete employees in removal list.
                    for (Employee employee : employeesToRemove) {
                        currBranch.leave(employee);
                    }

                }

            }
            myReader2.close();
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}