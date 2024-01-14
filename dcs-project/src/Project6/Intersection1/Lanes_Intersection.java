package Project6.Intersection1;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Lanes_Intersection {
  public static void main(String[] args) {

    PetriNet pn = new PetriNet();
    pn.PetriNetName = "Lanes Intersection";

    pn.NetworkPort = 1082;

    DataString full = new DataString();
    full.SetName("full");
    full.SetValue("full");
    pn.ConstantPlaceList.add(full);

    DataString green = new DataString();
    green.Printable = false;
    green.SetName("green");
    green.SetValue("green");
    pn.ConstantPlaceList.add(green);

    DataCarQueue p_i1 = new DataCarQueue();
    p_i1.Value.Size = 3;
    p_i1.SetName("P_i1");
    pn.PlaceList.add(p_i1);

    // -----------------------------------------------------------------------
    // -------------------------------STREET 1--------------------------------
    // -----------------------------------------------------------------------

    // -------------------------------INPUT LANE--------------------------------
    DataCar p_a1 = new DataCar();
    p_a1.SetName("P_a1");
    pn.PlaceList.add(p_a1);

    DataCarQueue p_x1 = new DataCarQueue();
    p_x1.Value.Size = 3;
    p_x1.SetName("P_x1");
    pn.PlaceList.add(p_x1);

    DataString p_tl1 = new DataString();
    p_tl1.SetName("P_TL1");
    pn.PlaceList.add(p_tl1);

    DataCar p_b1 = new DataCar();
    p_b1.SetName("P_b1");
    pn.PlaceList.add(p_b1);

    //Implementing OP1 as an output channel connected to the controller
    DataTransfer OP1 = new DataTransfer();
    OP1.SetName("OP1");
    OP1.Value = new TransferOperation("localhost", "1080", "in");
    pn.PlaceList.add(OP1);

    //-------Transitions-------

    // T_u1 -------------------
    PetriTransition t_u1 = new PetriTransition(pn);
    t_u1.TransitionName = "T_u1";
    t_u1.InputPlaceName.add("P_a1");
    t_u1.InputPlaceName.add("P_x1");

    Condition Tu1Ct1 = new Condition(t_u1, "P_a1", TransitionCondition.NotNull);
    Condition Tu1Ct2 = new Condition(t_u1, "P_x1", TransitionCondition.CanAddCars);
    Tu1Ct1.SetNextCondition(LogicConnector.AND, Tu1Ct2);

    GuardMapping grdTu1 = new GuardMapping();
    grdTu1.condition= Tu1Ct1;
    grdTu1.Activations.add(new Activation(t_u1, "P_a1", TransitionOperation.AddElement, "P_x1"));
    t_u1.GuardMappingList.add(grdTu1);

    Condition Tu1Ct3 = new Condition(t_u1, "P_a1", TransitionCondition.NotNull);
    Condition Tu1Ct4 = new Condition(t_u1, "P_x1", TransitionCondition.CanNotAddCars);
    Tu1Ct3.SetNextCondition(LogicConnector.AND, Tu1Ct4);

    GuardMapping grdTu1_1 = new GuardMapping();
    grdTu1_1.condition= Tu1Ct3;
    grdTu1_1.Activations.add(new Activation(t_u1, "full", TransitionOperation.SendOverNetwork, "OP1"));
    grdTu1_1.Activations.add(new Activation(t_u1, "P_a1", TransitionOperation.Move, "P_a1")); //Copy -> Move
    t_u1.GuardMappingList.add(grdTu1_1);

    t_u1.Delay = 0;
    pn.Transitions.add(t_u1);

    // T_u2 -----------------
    PetriTransition t_u2 = new PetriTransition(pn);
    t_u2.TransitionName = "T_u2";
    t_u2.InputPlaceName.add("P_x1");
    t_u2.InputPlaceName.add("P_TL1");

    Condition Tu2Ct1 = new Condition(t_u2, "P_TL1", TransitionCondition.Equal, "green");
    Condition Tu2Ct2 = new Condition(t_u2, "P_x1", TransitionCondition.HaveCar);
    Tu2Ct1.SetNextCondition(LogicConnector.AND, Tu2Ct2);

    GuardMapping grdTu2 = new GuardMapping();
    grdTu2.condition = Tu2Ct1;
    grdTu2.Activations.add(new Activation(t_u2, "P_x1", TransitionOperation.PopElementWithoutTarget, "P_b1"));
    grdTu2.Activations.add(new Activation(t_u2, "P_TL1", TransitionOperation.Move, "P_TL1"));

    t_u2.GuardMappingList.add(grdTu2);

//		t_u2.Delay = 3;
    pn.Transitions.add(t_u2);

    // T_i1 ---------------
    PetriTransition t_i1 = new PetriTransition(pn);
    t_i1.TransitionName = "T_i1";
    t_i1.InputPlaceName.add("P_b1");
    t_i1.InputPlaceName.add("P_i1");

    Condition Ti1_Ct1 = new Condition(t_i1, "P_b1", TransitionCondition.NotNull);
    Condition Ti1_Ct2 = new Condition(t_i1, "P_i1", TransitionCondition.CanAddCars);
    Ti1_Ct1.SetNextCondition(LogicConnector.AND, Ti1_Ct2);

    GuardMapping grdTi1 = new GuardMapping();
    grdTi1.condition = Ti1_Ct1;
    grdTi1.Activations.add(new Activation(t_i1, "P_b1", TransitionOperation.AddElement, "P_i1"));
    t_i1.GuardMappingList.add(grdTi1);

    t_i1.Delay = 0;
    pn.Transitions.add(t_i1);


    // -------------------------------EXIT LANE--------------------------------
    DataCarQueue p_o1 = new DataCarQueue();
    p_o1.Value.Size = 3;
    p_o1.SetName("P_o1");
    pn.PlaceList.add(p_o1);

    DataCar p_o2 = new DataCar();
    p_o2.SetName("P_o2");
    pn.PlaceList.add(p_o2);

    //-------Transitions-------

    // T_e1
    PetriTransition t_e1 = new PetriTransition(pn);
    t_e1.TransitionName = "T_e1";
    t_e1.InputPlaceName.add("P_i1");
    t_e1.InputPlaceName.add("P_o1");

    Condition Te1Ct1 = new Condition(t_e1, "P_i1", TransitionCondition.HaveCarForMe);
    Condition Te1Ct2 = new Condition(t_e1, "P_o1", TransitionCondition.CanAddCars);
    Te1Ct1.SetNextCondition(LogicConnector.AND, Te1Ct2);

    GuardMapping grdTe1 = new GuardMapping();
    grdTe1.condition = Te1Ct1;
    grdTe1.Activations.add(new Activation(t_e1, "P_i1", TransitionOperation.PopElementWithTargetToQueue, "P_o1"));
    t_e1.GuardMappingList.add(grdTe1);

    t_e1.Delay = 0;
    pn.Transitions.add(t_e1);

    // T_e2
    PetriTransition t_e2 = new PetriTransition(pn);
    t_e2.TransitionName = "T_e2";
    t_e2.InputPlaceName.add("P_o1");

    Condition Te2Ct1 = new Condition(t_e2, "P_o1", TransitionCondition.HaveCar);

    GuardMapping grdTe2 = new GuardMapping();
    grdTe2.condition = Te2Ct1;
    grdTe2.Activations.add(new Activation(t_e2, "P_o1", TransitionOperation.PopElementWithoutTarget, "P_o2"));
    t_e2.GuardMappingList.add(grdTe2);

    t_e2.Delay = 0;
    pn.Transitions.add(t_e2);


    // -----------------------------------------------------------------------
    // -------------------------------STREET 2--------------------------------
    // -----------------------------------------------------------------------

    // -------------------------------EXIT LANE--------------------------------
    DataCarQueue p_o3 = new DataCarQueue();
    p_o3.Value.Size = 3;
    p_o3.SetName("P_o3");
    pn.PlaceList.add(p_o3);

    DataCar p_o4 = new DataCar();
    p_o4.SetName("P_o4");
    pn.PlaceList.add(p_o4);

    //-------Transitions-------

    // T_e3
    PetriTransition t_e3 = new PetriTransition(pn);
    t_e3.TransitionName = "T_e3";
    t_e3.InputPlaceName.add("P_i1");
    t_e3.InputPlaceName.add("P_o3");

    Condition Te3Ct1 = new Condition(t_e3, "P_i1", TransitionCondition.HaveCarForMe);
    Condition Te3Ct2 = new Condition(t_e3, "P_o3", TransitionCondition.CanAddCars);
    Te3Ct1.SetNextCondition(LogicConnector.AND, Te3Ct2);

    GuardMapping grdTe3 = new GuardMapping();
    grdTe3.condition = Te3Ct1;
    grdTe3.Activations.add(new Activation(t_e3, "P_i1", TransitionOperation.PopElementWithTargetToQueue, "P_o3"));
    t_e3.GuardMappingList.add(grdTe3);

    t_e3.Delay = 0;
    pn.Transitions.add(t_e3);

    // T_e4
    PetriTransition t_e4 = new PetriTransition(pn);
    t_e4.TransitionName = "T_e4";
    t_e4.InputPlaceName.add("P_o3");

    Condition Te4Ct1 = new Condition(t_e4, "P_o3", TransitionCondition.HaveCar);

    GuardMapping grdTe4 = new GuardMapping();
    grdTe4.condition = Te4Ct1;
    grdTe4.Activations.add(new Activation(t_e4, "P_o3", TransitionOperation.PopElementWithoutTarget, "P_o4"));
    t_e4.GuardMappingList.add(grdTe4);

    t_e4.Delay = 0;
    pn.Transitions.add(t_e4);



    // -----------------------------------------------------------------------
    // -------------------------------STREET 3--------------------------------
    // -----------------------------------------------------------------------

    // -------------------------------INPUT LANE--------------------------------
    DataCar p_a2 = new DataCar();
    p_a2.SetName("P_a2");
    pn.PlaceList.add(p_a2);

    DataCarQueue p_x2 = new DataCarQueue();
    p_x2.Value.Size = 3;
    p_x2.SetName("P_x2");
    pn.PlaceList.add(p_x2);

    DataString p_tl2 = new DataString();
    p_tl2.SetName("P_TL2");
    pn.PlaceList.add(p_tl2);

    DataCar p_b2 = new DataCar();
    p_b2.SetName("P_b2");
    pn.PlaceList.add(p_b2);

    //Implementing OP2 as an output channel connected to the controller
    DataTransfer OP2 = new DataTransfer();
    OP2.SetName("OP2");
    OP2.Value = new TransferOperation("localhost", "1080", "in");
    pn.PlaceList.add(OP2);

    //-------Transitions-------

    // T_u4 -------------------
    PetriTransition t_u4 = new PetriTransition(pn);
    t_u4.TransitionName = "T_u4";
    t_u4.InputPlaceName.add("P_a2");
    t_u4.InputPlaceName.add("P_x2");

    Condition Tu4Ct1 = new Condition(t_u4, "P_a2", TransitionCondition.NotNull);
    Condition Tu4Ct2 = new Condition(t_u4, "P_x2", TransitionCondition.CanAddCars);
    Tu4Ct1.SetNextCondition(LogicConnector.AND, Tu4Ct2);

    GuardMapping grdTu4 = new GuardMapping();
    grdTu4.condition= Tu4Ct1;
    grdTu4.Activations.add(new Activation(t_u4, "P_a2", TransitionOperation.AddElement, "P_x2"));
    t_u4.GuardMappingList.add(grdTu4);

    Condition Tu4Ct3 = new Condition(t_u4, "P_a2", TransitionCondition.NotNull);
    Condition Tu4Ct4 = new Condition(t_u4, "P_x2", TransitionCondition.CanNotAddCars);
    Tu4Ct3.SetNextCondition(LogicConnector.AND, Tu4Ct4);

    GuardMapping grdTu4_1 = new GuardMapping();
    grdTu4_1.condition= Tu4Ct3;
    grdTu4_1.Activations.add(new Activation(t_u4, "full", TransitionOperation.SendOverNetwork, "OP2"));
    grdTu4_1.Activations.add(new Activation(t_u4, "P_a2", TransitionOperation.Move, "P_a2")); //Copy -> Move
    t_u4.GuardMappingList.add(grdTu4_1);

    t_u4.Delay = 0;
    pn.Transitions.add(t_u4);

    // T_u3 -----------------
    PetriTransition t_u3 = new PetriTransition(pn);
    t_u3.TransitionName = "T_u3";
    t_u3.InputPlaceName.add("P_x2");
    t_u3.InputPlaceName.add("P_TL2");

    Condition Tu3Ct1 = new Condition(t_u3, "P_TL2", TransitionCondition.Equal, "green");
    Condition Tu3Ct2 = new Condition(t_u3, "P_x2", TransitionCondition.HaveCar);
    Tu3Ct1.SetNextCondition(LogicConnector.AND, Tu3Ct2);

    GuardMapping grdTu3 = new GuardMapping();
    grdTu3.condition = Tu3Ct1;
    grdTu3.Activations.add(new Activation(t_u3, "P_x2", TransitionOperation.PopElementWithoutTarget, "P_b2"));
    grdTu3.Activations.add(new Activation(t_u3, "P_TL2", TransitionOperation.Move, "P_TL2"));

    t_u3.GuardMappingList.add(grdTu3);

//		t_u3.Delay = 3;
    pn.Transitions.add(t_u3);

    // T_i2 ---------------
    PetriTransition t_i2 = new PetriTransition(pn);
    t_i2.TransitionName = "T_i2";
    t_i2.InputPlaceName.add("P_b2");
    t_i2.InputPlaceName.add("P_i1");

    Condition Ti2_Ct1 = new Condition(t_i2, "P_b2", TransitionCondition.NotNull);
    Condition Ti2_Ct2 = new Condition(t_i2, "P_i1", TransitionCondition.CanAddCars);
    Ti2_Ct1.SetNextCondition(LogicConnector.AND, Ti2_Ct2);

    GuardMapping grdTi2 = new GuardMapping();
    grdTi2.condition = Ti2_Ct1;
    grdTi2.Activations.add(new Activation(t_i2, "P_b2", TransitionOperation.AddElement, "P_i1"));
    t_i2.GuardMappingList.add(grdTi2);

    t_i2.Delay = 0;
    pn.Transitions.add(t_i2);


    // -------------------------------EXIT LANE--------------------------------
    DataCarQueue p_o5 = new DataCarQueue();
    p_o5.Value.Size = 3;
    p_o5.SetName("P_o5");
    pn.PlaceList.add(p_o5);

    DataCar p_o6 = new DataCar();
    p_o6.SetName("P_o6");
    pn.PlaceList.add(p_o6);

//    DataCar p_street1 = new DataCar();
//    p_street1.SetName("P_street1");
//    pn.PlaceList.add(p_street1);


    DataTransfer p_street1 = new DataTransfer();
    p_street1.SetName("P_street1");
    p_street1.Value = new TransferOperation("localhost", "1083", "P_a4");
    pn.PlaceList.add(p_street1);

    //-------Transitions-------

    // T_e5
    PetriTransition t_e5 = new PetriTransition(pn);
    t_e5.TransitionName = "T_e5";
    t_e5.InputPlaceName.add("P_i1");
    t_e5.InputPlaceName.add("P_o5");

    Condition Te5Ct1 = new Condition(t_e5, "P_i1", TransitionCondition.HaveCarForMe);
    Condition Te5Ct2 = new Condition(t_e5, "P_o5", TransitionCondition.CanAddCars);
    Te5Ct1.SetNextCondition(LogicConnector.AND, Te5Ct2);

    GuardMapping grdTe5 = new GuardMapping();
    grdTe5.condition = Te5Ct1;
    grdTe5.Activations.add(new Activation(t_e5, "P_i1", TransitionOperation.PopElementWithTargetToQueue, "P_o5"));
    t_e5.GuardMappingList.add(grdTe5);

    t_e5.Delay = 0;
    pn.Transitions.add(t_e5);

    // T_e6
    PetriTransition t_e6 = new PetriTransition(pn);
    t_e6.TransitionName = "T_e6";
    t_e6.InputPlaceName.add("P_o5");

    Condition Te6Ct1 = new Condition(t_e6, "P_o5", TransitionCondition.HaveCar);

    GuardMapping grdTe6 = new GuardMapping();
    grdTe6.condition = Te6Ct1;
    grdTe6.Activations.add(new Activation(t_e6, "P_o5", TransitionOperation.PopElementWithoutTarget, "P_o6"));
    t_e6.GuardMappingList.add(grdTe6);

    t_e6.Delay = 0;
    pn.Transitions.add(t_e6);

    // T_street1
    PetriTransition t_street1 = new PetriTransition(pn);
    t_street1.TransitionName = "T_street1";
    t_street1.InputPlaceName.add("P_o6");

    Condition T_street1_Ct = new Condition(t_street1, "P_o6", TransitionCondition.NotNull);

    GuardMapping grdTstreet1 = new GuardMapping();
    grdTstreet1.condition = T_street1_Ct;
    grdTstreet1.Activations.add(new Activation(t_street1, "P_o6", TransitionOperation.SendOverNetwork, "P_street1"));
    t_street1.GuardMappingList.add(grdTstreet1);
    t_street1.Delay = 0;
    pn.Transitions.add(t_street1);

//    // T_street2
//    PetriTransition t_street2 = new PetriTransition(pn);
//    t_street2.TransitionName = "T_street2";
//    t_street2.InputPlaceName.add("P_street1");
//
//    Condition T_street2_Ct = new Condition(t_street2, "P_street1", TransitionCondition.NotNull);
//
//    GuardMapping grdTstreet2 = new GuardMapping();
//    grdTstreet2.condition = T_street2_Ct;
//    grdTstreet2.Activations.add(new Activation(t_street2, "P_street1", TransitionOperation.SendOverNetwork, "P_a4"));


    // -----------------------------------------------------------------------
    // -------------------------------STREET 4--------------------------------
    // -----------------------------------------------------------------------

    // -------------------------------INPUT LANE--------------------------------
    DataCar p_a3 = new DataCar();
    p_a3.SetName("P_a3");
    pn.PlaceList.add(p_a3);

    DataCarQueue p_x3 = new DataCarQueue();
    p_x3.Value.Size = 3;
    p_x3.SetName("P_x3");
    pn.PlaceList.add(p_x3);

    DataString p_tl3 = new DataString();
    p_tl3.SetName("P_TL3");
    pn.PlaceList.add(p_tl3);

    DataCar p_b3 = new DataCar();
    p_b3.SetName("P_b3");
    pn.PlaceList.add(p_b3);

    //Implementing OP3 as an output channel connected to the controller
    DataTransfer OP3 = new DataTransfer();
    OP3.SetName("OP3");
    OP3.Value = new TransferOperation("localhost", "1080", "in");
    pn.PlaceList.add(OP3);

    //-------Transitions-------

    // T_u6 -------------------
    PetriTransition t_u6 = new PetriTransition(pn);
    t_u6.TransitionName = "T_u6";
    t_u6.InputPlaceName.add("P_a3");
    t_u6.InputPlaceName.add("P_x3");

    Condition Tu6Ct1 = new Condition(t_u6, "P_a3", TransitionCondition.NotNull);
    Condition Tu6Ct2 = new Condition(t_u6, "P_x3", TransitionCondition.CanAddCars);
    Tu6Ct1.SetNextCondition(LogicConnector.AND, Tu6Ct2);

    GuardMapping grdTu6 = new GuardMapping();
    grdTu6.condition= Tu6Ct1;
    grdTu6.Activations.add(new Activation(t_u6, "P_a3", TransitionOperation.AddElement, "P_x3"));
    t_u6.GuardMappingList.add(grdTu6);

    Condition Tu6Ct3 = new Condition(t_u6, "P_a3", TransitionCondition.NotNull);
    Condition Tu6Ct4 = new Condition(t_u6, "P_x3", TransitionCondition.CanNotAddCars);
    Tu6Ct3.SetNextCondition(LogicConnector.AND, Tu6Ct4);

    GuardMapping grdTu6_1 = new GuardMapping();
    grdTu6_1.condition= Tu6Ct3;
    grdTu6_1.Activations.add(new Activation(t_u6, "full", TransitionOperation.SendOverNetwork, "OP3"));
    grdTu6_1.Activations.add(new Activation(t_u6, "P_a3", TransitionOperation.Move, "P_a3")); //Copy -> Move
    t_u6.GuardMappingList.add(grdTu6_1);

    t_u6.Delay = 0;
    pn.Transitions.add(t_u6);

    // T_u5 -----------------
    PetriTransition t_u5 = new PetriTransition(pn);
    t_u5.TransitionName = "T_u5";
    t_u5.InputPlaceName.add("P_x3");
    t_u5.InputPlaceName.add("P_TL3");

    Condition Tu5Ct1 = new Condition(t_u5, "P_TL3", TransitionCondition.Equal, "green");
    Condition Tu5Ct2 = new Condition(t_u5, "P_x3", TransitionCondition.HaveCar);
    Tu5Ct1.SetNextCondition(LogicConnector.AND, Tu5Ct2);

    GuardMapping grdTu5 = new GuardMapping();
    grdTu5.condition = Tu5Ct1;
    grdTu5.Activations.add(new Activation(t_u5, "P_x3", TransitionOperation.PopElementWithoutTarget, "P_b3"));
    grdTu5.Activations.add(new Activation(t_u5, "P_TL3", TransitionOperation.Move, "P_TL3"));

    t_u5.GuardMappingList.add(grdTu5);

//		t_u5.Delay = 3;
    pn.Transitions.add(t_u5);

    // T_i3 ---------------
    PetriTransition t_i3 = new PetriTransition(pn);
    t_i3.TransitionName = "T_i3";
    t_i3.InputPlaceName.add("P_b3");
    t_i3.InputPlaceName.add("P_i1");

    Condition Ti3_Ct1 = new Condition(t_i3, "P_b3", TransitionCondition.NotNull);
    Condition Ti3_Ct2 = new Condition(t_i3, "P_i1", TransitionCondition.CanAddCars);
    Ti3_Ct1.SetNextCondition(LogicConnector.AND, Ti3_Ct2);

    GuardMapping grdTi3 = new GuardMapping();
    grdTi3.condition = Ti3_Ct1;
    grdTi3.Activations.add(new Activation(t_i3, "P_b3", TransitionOperation.AddElement, "P_i1"));
    t_i3.GuardMappingList.add(grdTi3);

    t_i3.Delay = 0;
    pn.Transitions.add(t_i3);



    // -------------------------------------------------------------------------------------
    // ----------------------------PNStart-------------------------------------------------
    // -------------------------------------------------------------------------------------

    System.out.println("Exp1 started \n ------------------------------");
    pn.Delay = 2000;
    // pn.Start();

    PetriNetWindow frame = new PetriNetWindow(false);
    frame.petriNet = pn;
    frame.setVisible(true);
  }
}
