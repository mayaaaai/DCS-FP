package Project6.Intersection2;

import Components.*;
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

        pn.NetworkPort = 1083;

        DataString full = new DataString();
        full.SetName("full");
        full.SetValue("full");
        pn.ConstantPlaceList.add(full);

        DataString green = new DataString();
        green.Printable = false;
        green.SetName("green");
        green.SetValue("green");
        pn.ConstantPlaceList.add(green);

        DataCarQueue p_i2 = new DataCarQueue();
        p_i2.Value.Size = 3;
        p_i2.SetName("P_i2");
        pn.PlaceList.add(p_i2);


        // -----------------------------------------------------------------------
        // -------------------------------STREET 1--------------------------------
        // -----------------------------------------------------------------------

        // -------------------------------INPUT LANE--------------------------------
        DataCar p_a4 = new DataCar();
        p_a4.SetName("P_a4");
        pn.PlaceList.add(p_a4);

        DataCarQueue p_x4 = new DataCarQueue();
        p_x4.Value.Size = 3;
        p_x4.SetName("P_x4");
        pn.PlaceList.add(p_x4);

        DataString p_tl4 = new DataString();
        p_tl4.SetName("P_TL4");
        pn.PlaceList.add(p_tl4);

        DataCar p_b4 = new DataCar();
        p_b4.SetName("P_b4");
        pn.PlaceList.add(p_b4);

        //Implementing OP4 as an output channel connected to the controller
        DataTransfer OP4 = new DataTransfer();
        OP4.SetName("OP4");
        OP4.Value = new TransferOperation("localhost", "1081", "in");
        pn.PlaceList.add(OP4);


        //-------Transitions-------

        // T_u7 -------------------
        PetriTransition t_u7 = new PetriTransition(pn);
        t_u7.TransitionName = "T_u7";
        t_u7.InputPlaceName.add("P_a4");
        t_u7.InputPlaceName.add("P_x4");

        Condition Tu7Ct1 = new Condition(t_u7, "P_a4", TransitionCondition.NotNull);
        Condition Tu7Ct2 = new Condition(t_u7, "P_x4", TransitionCondition.CanAddCars);
        Tu7Ct1.SetNextCondition(LogicConnector.AND, Tu7Ct2);

        GuardMapping grdTu7 = new GuardMapping();
        grdTu7.condition= Tu7Ct1;
        grdTu7.Activations.add(new Activation(t_u7, "P_a4", TransitionOperation.AddElement, "P_x4"));
        t_u7.GuardMappingList.add(grdTu7);

        Condition Tu7Ct3 = new Condition(t_u7, "P_a4", TransitionCondition.NotNull);
        Condition Tu7Ct4 = new Condition(t_u7, "P_x4", TransitionCondition.CanNotAddCars);
        Tu7Ct3.SetNextCondition(LogicConnector.AND, Tu7Ct4);

        GuardMapping grdTu7_1 = new GuardMapping();
        grdTu7_1.condition= Tu7Ct3;
        grdTu7_1.Activations.add(new Activation(t_u7, "full", TransitionOperation.SendOverNetwork, "OP4"));
        grdTu7_1.Activations.add(new Activation(t_u7, "P_a4", TransitionOperation.Move, "P_a4")); //Copy -> Move
        t_u7.GuardMappingList.add(grdTu7_1);

        t_u7.Delay = 0;
        pn.Transitions.add(t_u7);

        // T_u8 -----------------
        PetriTransition t_u8 = new PetriTransition(pn);
        t_u8.TransitionName = "T_u8";
        t_u8.InputPlaceName.add("P_x4");
        t_u8.InputPlaceName.add("P_TL4");

        Condition Tu8Ct1 = new Condition(t_u8, "P_TL4", TransitionCondition.Equal, "green");
        Condition Tu8Ct2 = new Condition(t_u8, "P_x4", TransitionCondition.HaveCar);
        Tu8Ct1.SetNextCondition(LogicConnector.AND, Tu8Ct2);

        GuardMapping grdTu8 = new GuardMapping();
        grdTu8.condition = Tu8Ct1;
        grdTu8.Activations.add(new Activation(t_u8, "P_x4", TransitionOperation.PopElementWithoutTarget, "P_b4"));
        grdTu8.Activations.add(new Activation(t_u8, "P_TL4", TransitionOperation.Move, "P_TL4"));

        t_u8.GuardMappingList.add(grdTu8);

//		t_u2.Delay = 3;
        pn.Transitions.add(t_u8);

        // T_i4 ---------------
        PetriTransition t_i4 = new PetriTransition(pn);
        t_i4.TransitionName = "T_i4";
        t_i4.InputPlaceName.add("P_b4");
        t_i4.InputPlaceName.add("P_i2");

        Condition Ti4_Ct1 = new Condition(t_i4, "P_b4", TransitionCondition.NotNull);
        Condition Ti4_Ct2 = new Condition(t_i4, "P_i2", TransitionCondition.CanAddCars);
        Ti4_Ct1.SetNextCondition(LogicConnector.AND, Ti4_Ct2);

        GuardMapping grdTi4 = new GuardMapping();
        grdTi4.condition = Ti4_Ct1;
        grdTi4.Activations.add(new Activation(t_i4, "P_b1", TransitionOperation.AddElement, "P_i2"));
        t_i4.GuardMappingList.add(grdTi4);

        t_i4.Delay = 0;
        pn.Transitions.add(t_i4);

        // -------------------------------EXIT LANE--------------------------------
        DataCarQueue p_o7 = new DataCarQueue();
        p_o7.Value.Size = 3;
        p_o7.SetName("P_o7");
        pn.PlaceList.add(p_o7);

        DataCar p_o8 = new DataCar();
        p_o8.SetName("P_o8");
        pn.PlaceList.add(p_o8);

        DataTransfer p_street2 = new DataTransfer();
        p_street2.SetName("P_street2");
        p_street2.Value = new TransferOperation("localhost", "1082", "P_a2");
        pn.PlaceList.add(p_street2);

        //-------Transitions-------

        // T_e7
        PetriTransition t_e7 = new PetriTransition(pn);
        t_e7.TransitionName = "T_e7";
        t_e7.InputPlaceName.add("P_i2");
        t_e7.InputPlaceName.add("P_o7");

        Condition Te7Ct1 = new Condition(t_e7, "P_i2", TransitionCondition.HaveCarForMe);
        Condition Te7Ct2 = new Condition(t_e7, "P_o7", TransitionCondition.CanAddCars);
        Te7Ct1.SetNextCondition(LogicConnector.AND, Te7Ct2);

        GuardMapping grdTe7 = new GuardMapping();
        grdTe7.condition = Te7Ct1;
        grdTe7.Activations.add(new Activation(t_e7, "P_i2", TransitionOperation.PopElementWithTargetToQueue, "P_o7"));
        t_e7.GuardMappingList.add(grdTe7);

        t_e7.Delay = 0;
        pn.Transitions.add(t_e7);

        // T_e8
        PetriTransition t_e8 = new PetriTransition(pn);
        t_e8.TransitionName = "t_e8";
        t_e8.InputPlaceName.add("P_o7");

        Condition Te8Ct1 = new Condition(t_e8, "P_o7", TransitionCondition.HaveCar);

        GuardMapping grdTe8 = new GuardMapping();
        grdTe8.condition = Te8Ct1;
        grdTe8.Activations.add(new Activation(t_e8, "P_o7", TransitionOperation.PopElementWithoutTarget, "P_o8"));
        t_e8.GuardMappingList.add(grdTe8);

        t_e8.Delay = 0;
        pn.Transitions.add(t_e8);

        // T_street3
        PetriTransition t_street3 = new PetriTransition(pn);
        t_street3.TransitionName = "T_street3";
        t_street3.InputPlaceName.add("P_o8");

        Condition T_street3_Ct = new Condition(t_street3, "P_o8", TransitionCondition.NotNull);

        GuardMapping grdTstreet3 = new GuardMapping();
        grdTstreet3.condition = T_street3_Ct;
        grdTstreet3.Activations.add(new Activation(t_street3, "P_o8", TransitionOperation.Move, "P_street2"));


        // T_street4
        PetriTransition t_street4 = new PetriTransition(pn);
        t_street4.TransitionName = "T_street4";
        t_street4.InputPlaceName.add("P_street2");

        Condition T_street4_Ct = new Condition(t_street4, "P_street2", TransitionCondition.NotNull);

        GuardMapping grdTstreet4 = new GuardMapping();
        grdTstreet4.condition = T_street4_Ct;
        grdTstreet4.Activations.add(new Activation(t_street4, "P_street2", TransitionOperation.SendOverNetwork, "P_a2"));



        // -----------------------------------------------------------------------
        // -------------------------------STREET 2--------------------------------
        // -----------------------------------------------------------------------

        // -------------------------------EXIT LANE--------------------------------
        DataCarQueue p_o11 = new DataCarQueue();
        p_o11.Value.Size = 3;
        p_o11.SetName("P_o11");
        pn.PlaceList.add(p_o11);

        DataCar p_o12 = new DataCar();
        p_o12.SetName("P_o12");
        pn.PlaceList.add(p_o12);

        //-------Transitions-------

        // T_e3
        PetriTransition t_e11 = new PetriTransition(pn);
        t_e11.TransitionName = "T_e11";
        t_e11.InputPlaceName.add("P_i2");
        t_e11.InputPlaceName.add("P_o11");

        Condition Te11Ct1 = new Condition(t_e11, "P_i2", TransitionCondition.HaveCarForMe);
        Condition Te11Ct2 = new Condition(t_e11, "P_o11", TransitionCondition.CanAddCars);
        Te11Ct1.SetNextCondition(LogicConnector.AND, Te11Ct2);

        GuardMapping grdTe11 = new GuardMapping();
        grdTe11.condition = Te11Ct1;
        grdTe11.Activations.add(new Activation(t_e11, "P_i2", TransitionOperation.PopElementWithTargetToQueue, "P_o11"));
        t_e11.GuardMappingList.add(grdTe11);

        t_e11.Delay = 0;
        pn.Transitions.add(t_e11);

        // T_e4
        PetriTransition t_e12 = new PetriTransition(pn);
        t_e12.TransitionName = "t_e12";
        t_e12.InputPlaceName.add("P_o11");

        Condition Te12Ct1 = new Condition(t_e12, "P_o11", TransitionCondition.HaveCar);

        GuardMapping grdTe12 = new GuardMapping();
        grdTe12.condition = Te12Ct1;
        grdTe12.Activations.add(new Activation(t_e12, "P_o11", TransitionOperation.PopElementWithoutTarget, "P_o12"));
        t_e12.GuardMappingList.add(grdTe12);

        t_e12.Delay = 0;
        pn.Transitions.add(t_e12);

        // -----------------------------------------------------------------------
        // -------------------------------STREET 3--------------------------------
        // -----------------------------------------------------------------------

        // -------------------------------INPUT LANE--------------------------------
        DataCar p_a6 = new DataCar();
        p_a6.SetName("P_a6");
        pn.PlaceList.add(p_a6);

        DataCarQueue p_x6 = new DataCarQueue();
        p_x6.Value.Size = 3;
        p_x6.SetName("P_x6");
        pn.PlaceList.add(p_x6);

        DataString p_tl6 = new DataString();
        p_tl6.SetName("P_TL6");
        pn.PlaceList.add(p_tl6);

        DataCar p_b6 = new DataCar();
        p_b6.SetName("P_b6");
        pn.PlaceList.add(p_b6);

        //Implementing OP2 as an output channel connected to the controller
        DataTransfer OP6 = new DataTransfer();
        OP6.SetName("OP6");
        OP6.Value = new TransferOperation("localhost", "1081", "in");
        pn.PlaceList.add(OP6);

        //-------Transitions-------

        // T_u11 -------------------
        PetriTransition t_u11 = new PetriTransition(pn);
        t_u11.TransitionName = "T_u11";
        t_u11.InputPlaceName.add("P_a6");
        t_u11.InputPlaceName.add("P_x6");

        Condition Tu11Ct1 = new Condition(t_u11, "P_a2", TransitionCondition.NotNull);
        Condition Tu11Ct2 = new Condition(t_u11, "P_x6", TransitionCondition.CanAddCars);
        Tu11Ct1.SetNextCondition(LogicConnector.AND, Tu11Ct2);

        GuardMapping grdTu11 = new GuardMapping();
        grdTu11.condition= Tu11Ct1;
        grdTu11.Activations.add(new Activation(t_u11, "P_a6", TransitionOperation.AddElement, "P_x6"));
        t_u11.GuardMappingList.add(grdTu11);

        Condition Tu11Ct3 = new Condition(t_u11, "P_a6", TransitionCondition.NotNull);
        Condition Tu11Ct4 = new Condition(t_u11, "P_x6", TransitionCondition.CanNotAddCars);
        Tu11Ct3.SetNextCondition(LogicConnector.AND, Tu11Ct4);

        GuardMapping grdTu11_1 = new GuardMapping();
        grdTu11_1.condition= Tu11Ct3;
        grdTu11_1.Activations.add(new Activation(t_u11, "full", TransitionOperation.SendOverNetwork, "OP6"));
        grdTu11_1.Activations.add(new Activation(t_u11, "P_a2", TransitionOperation.Move, "P_a6")); //Copy -> Move
        t_u11.GuardMappingList.add(grdTu11_1);

        t_u11.Delay = 0;
        pn.Transitions.add(t_u11);

        // T_u12 -----------------
        PetriTransition t_u12 = new PetriTransition(pn);
        t_u12.TransitionName = "T_u12";
        t_u12.InputPlaceName.add("P_x6");
        t_u12.InputPlaceName.add("P_TL6");

        Condition Tu12Ct1 = new Condition(t_u12, "P_TL6", TransitionCondition.Equal, "green");
        Condition Tu12Ct2 = new Condition(t_u12, "P_x6", TransitionCondition.HaveCar);
        Tu12Ct1.SetNextCondition(LogicConnector.AND, Tu12Ct2);

        GuardMapping grdTu12 = new GuardMapping();
        grdTu12.condition = Tu12Ct1;
        grdTu12.Activations.add(new Activation(t_u12, "P_x2", TransitionOperation.PopElementWithoutTarget, "P_b6"));
        grdTu12.Activations.add(new Activation(t_u12, "P_TL6", TransitionOperation.Move, "P_TL6"));

        t_u12.GuardMappingList.add(grdTu12);

//		t_u12.Delay = 3;
        pn.Transitions.add(t_u12);

        // T_i6 ---------------
        PetriTransition t_i6 = new PetriTransition(pn);
        t_i6.TransitionName = "T_i6";
        t_i6.InputPlaceName.add("P_b6");
        t_i6.InputPlaceName.add("P_i2");

        Condition Ti6_Ct1 = new Condition(t_i6, "P_b6", TransitionCondition.NotNull);
        Condition Ti2_Ct2 = new Condition(t_i6, "P_i2", TransitionCondition.CanAddCars);
        Ti6_Ct1.SetNextCondition(LogicConnector.AND, Ti2_Ct2);

        GuardMapping grdTi6 = new GuardMapping();
        grdTi6.condition = Ti6_Ct1;
        grdTi6.Activations.add(new Activation(t_i6, "P_b6", TransitionOperation.AddElement, "P_i2"));
        t_i6.GuardMappingList.add(grdTi6);

        t_i6.Delay = 0;
        pn.Transitions.add(t_i6);

        // T_street4
//    PetriTransition t_street4 = new PetriTransition(pn);
//    t_street4.TransitionName = "T_street4";
//    t_street4.InputPlaceName.add("P_street2");
//
//    Condition T_street4_Ct = new Condition(t_street4, "P_street2", TransitionCondition.NotNull);
//
//    GuardMapping grdTstreet4 = new GuardMapping();
//    grdTstreet4.condition = T_street4_Ct;
//    grdTstreet4.Activations.add(new Activation(t_street4, "P_street2", TransitionOperation.SendOverNetwork, "P_street2"));



        // -----------------------------------------------------------------------
        // -------------------------------STREET 4--------------------------------
        // -----------------------------------------------------------------------

        // -------------------------------INPUT LANE--------------------------------
        DataCar p_a5 = new DataCar();
        p_a5.SetName("P_a5");
        pn.PlaceList.add(p_a5);

        DataCarQueue p_x5 = new DataCarQueue();
        p_x5.Value.Size = 3;
        p_x5.SetName("P_x5");
        pn.PlaceList.add(p_x5);

        DataString p_tl5 = new DataString();
        p_tl5.SetName("P_TL5");
        pn.PlaceList.add(p_tl5);

        DataCar p_b5 = new DataCar();
        p_b5.SetName("P_b5");
        pn.PlaceList.add(p_b5);

        //Implementing OP3 as an output channel connected to the controller
        DataTransfer OP5 = new DataTransfer();
        OP5.SetName("OP3");
        OP5.Value = new TransferOperation("localhost", "1081", "in");
        pn.PlaceList.add(OP5);

        //-------Transitions-------

        // T_u6 -------------------
        PetriTransition t_u9 = new PetriTransition(pn);
        t_u9.TransitionName = "T_u9";
        t_u9.InputPlaceName.add("P_a5");
        t_u9.InputPlaceName.add("P_x5");

        Condition Tu9Ct1 = new Condition(t_u9, "P_a5", TransitionCondition.NotNull);
        Condition Tu9Ct2 = new Condition(t_u9, "P_x5", TransitionCondition.CanAddCars);
        Tu9Ct1.SetNextCondition(LogicConnector.AND, Tu9Ct2);

        GuardMapping grdTu9 = new GuardMapping();
        grdTu9.condition= Tu9Ct1;
        grdTu9.Activations.add(new Activation(t_u9, "P_a5", TransitionOperation.AddElement, "P_x5"));
        t_u9.GuardMappingList.add(grdTu9);

        Condition Tu9Ct3 = new Condition(t_u9, "P_a5", TransitionCondition.NotNull);
        Condition Tu9Ct4 = new Condition(t_u9, "P_x5", TransitionCondition.CanNotAddCars);
        Tu9Ct3.SetNextCondition(LogicConnector.AND, Tu9Ct4);

        GuardMapping grdTu9_1 = new GuardMapping();
        grdTu9_1.condition= Tu9Ct3;
        grdTu9_1.Activations.add(new Activation(t_u9, "full", TransitionOperation.SendOverNetwork, "OP5"));
        grdTu9_1.Activations.add(new Activation(t_u9, "P_a5", TransitionOperation.Move, "P_a5")); //Copy -> Move
        t_u9.GuardMappingList.add(grdTu9_1);

        t_u9.Delay = 0;
        pn.Transitions.add(t_u9);

        // T_u10 -----------------
        PetriTransition t_u10 = new PetriTransition(pn);
        t_u10.TransitionName = "T_u10";
        t_u10.InputPlaceName.add("P_x5");
        t_u10.InputPlaceName.add("P_TL5");

        Condition Tu10Ct1 = new Condition(t_u10, "P_TL5", TransitionCondition.Equal, "green");
        Condition Tu10Ct2 = new Condition(t_u10, "P_x5", TransitionCondition.HaveCar);
        Tu10Ct1.SetNextCondition(LogicConnector.AND, Tu10Ct2);

        GuardMapping grdTu10 = new GuardMapping();
        grdTu10.condition = Tu10Ct1;
        grdTu10.Activations.add(new Activation(t_u10, "P_x5", TransitionOperation.PopElementWithoutTarget, "P_b5"));
        grdTu10.Activations.add(new Activation(t_u10, "P_TL5", TransitionOperation.Move, "P_TL5"));

        t_u10.GuardMappingList.add(grdTu10);

//		t_u5.Delay = 3;
        pn.Transitions.add(t_u10);

        // T_i5 ---------------
        PetriTransition t_i5 = new PetriTransition(pn);
        t_i5.TransitionName = "T_i5";
        t_i5.InputPlaceName.add("P_b5");
        t_i5.InputPlaceName.add("P_i2");

        Condition Ti5_Ct1 = new Condition(t_i5, "P_b5", TransitionCondition.NotNull);
        Condition Ti5_Ct2 = new Condition(t_i5, "P_i2", TransitionCondition.CanAddCars);
        Ti5_Ct1.SetNextCondition(LogicConnector.AND, Ti5_Ct2);

        GuardMapping grdTi5 = new GuardMapping();
        grdTi5.condition = Ti5_Ct1;
        grdTi5.Activations.add(new Activation(t_i5, "P_b5", TransitionOperation.AddElement, "P_i2"));
        t_i5.GuardMappingList.add(grdTi5);

        t_i5.Delay = 0;
        pn.Transitions.add(t_i5);

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
