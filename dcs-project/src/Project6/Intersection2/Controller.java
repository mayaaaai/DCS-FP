package Project6.Intersection2;

import Components.*;
import DataObjects.DataInteger;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Controller {

    public static void main (String []args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller";
        pn.SetName("Controller");
        pn.NetworkPort = 1081;

        DataString ini = new DataString();
        //ini.Printable = false;
        ini.SetName("ini");
        ini.SetValue("red");
        pn.ConstantPlaceList.add(ini);

        DataString in4 = new DataString();
        in4.SetName("in4");
        pn.PlaceList.add(in4);

        DataString in5 = new DataString();
        in5.SetName("in5");
        pn.PlaceList.add(in5);

        DataString in6 = new DataString();
        in6.SetName("in6");
        pn.PlaceList.add(in6);


        DataString red = new DataString();
        //red.Printable = false;
        red.SetName("red");
        red.SetValue("red");
        pn.ConstantPlaceList.add(red);

        DataString green = new DataString();
        //green.Printable = false;
        green.SetName("green");
        green.SetValue("green");
        pn.ConstantPlaceList.add(green);

        DataString yellow = new DataString();
        //yellow.Printable = false;
        yellow.SetName("yellow");
        yellow.SetValue("yellow");
        pn.ConstantPlaceList.add(yellow);

        DataString p1 = new DataString();
        p1.SetName("r1r2r3");
        p1.SetValue("signal");
        pn.PlaceList.add(p1);

        DataString p2 = new DataString();
        p2.SetName("g1r2r3");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("y1r2r3");
        pn.PlaceList.add(p3);

        DataString p4 = new DataString();
        p4.SetName("r1g2r3");
        pn.PlaceList.add(p4);

        DataString p5 = new DataString();
        p5.SetName("r1y2r3");
        pn.PlaceList.add(p5);

        DataString p6 = new DataString();
        p6.SetName("r1r2g3");
        pn.PlaceList.add(p6);

        DataString p7 = new DataString();
        p7.SetName("r1r2y3");
        pn.PlaceList.add(p7);


        DataTransfer p8 = new DataTransfer();
        p8.SetName("OP4");
        p8.Value = new TransferOperation("localhost", "1080" , "P_TL4");
        pn.PlaceList.add(p8);

        DataTransfer p9 = new DataTransfer();
        p9.SetName("OP5");
        p9.Value = new TransferOperation("localhost", "1080" , "P_TL5");
        pn.PlaceList.add(p9);

        DataTransfer p10 = new DataTransfer();
        p10.SetName("OP6");
        p10.Value = new TransferOperation("localhost", "1080" , "P_TL6");
        pn.PlaceList.add(p10);

        DataInteger Five = new DataInteger();
        Five.SetName("Five");
        Five.SetValue(500);
        pn.ConstantPlaceList.add(Five);

        DataInteger Ten = new DataInteger();
        Ten.SetName("Ten");
        Ten.SetValue(1000);
        pn.ConstantPlaceList.add(Ten);




        //----------------------------iniT------------------------------------
        PetriTransition tini = new PetriTransition(pn);
        tini.TransitionName = "tini";

        Condition iniTCt1 = new Condition(tini, "ini", TransitionCondition.NotNull);

        GuardMapping grdiniT = new GuardMapping();
        grdiniT.condition= iniTCt1;

        grdiniT.Activations.add(new Activation(tini, "ini", TransitionOperation.SendOverNetwork, "OP4"));
        grdiniT.Activations.add(new Activation(tini, "ini", TransitionOperation.SendOverNetwork, "OP5"));
        grdiniT.Activations.add(new Activation(tini, "ini", TransitionOperation.SendOverNetwork, "OP6"));
        grdiniT.Activations.add(new Activation(tini, "", TransitionOperation.MakeNull, "ini"));

        tini.GuardMappingList.add(grdiniT);

        tini.Delay = 0;
        pn.Transitions.add(tini);


        //----------------------------T1------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("r1r2r3");


        Condition T1Ct1 = new Condition(t1, "r1r2r3", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition= T1Ct1;
        grdT1.Activations.add(new Activation(t1, "r1r2r3", TransitionOperation.Move, "g1r2r3"));
        grdT1.Activations.add(new Activation(t1, "green", TransitionOperation.SendOverNetwork, "OP4"));
        grdT1.Activations.add(new Activation(t1, "Ten", TransitionOperation.DynamicDelay,""));
        t1.GuardMappingList.add(grdT1);


        pn.Transitions.add(t1);

        //----------------------------T2------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("g1r2r3");


        Condition T2Ct1 = new Condition(t2, "g1r2r3", TransitionCondition.NotNull);
        Condition T2Ct2 = new Condition(t2, "in4", TransitionCondition.NotNull);
        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition= T2Ct1;
        grdT2.Activations.add(new Activation(t2, "g1r2r3", TransitionOperation.Move, "y1r2r3"));
        grdT2.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP4"));
        grdT2.Activations.add(new Activation(t2, "Ten", TransitionOperation.DynamicDelay,""));


        Condition T2Ct3 = new Condition(t2, "g1r2r3", TransitionCondition.NotNull);
        Condition T2Ct4 = new Condition(t2, "in4", TransitionCondition.IsNull);
        T2Ct3.SetNextCondition(LogicConnector.AND, T2Ct4);

        GuardMapping grdT22 = new GuardMapping();
        grdT22.condition= T2Ct3;
        grdT22.Activations.add(new Activation(t2, "g1r2r3", TransitionOperation.Move, "y1r2r3"));
        grdT22.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP5"));
        grdT22.Activations.add(new Activation(t2, "Five", TransitionOperation.DynamicDelay,""));


        t2.GuardMappingList.add(grdT2);
        t2.GuardMappingList.add(grdT22);

        pn.Transitions.add(t2);


        //----------------------------T3------------------------------------
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("y1r2r3");



        Condition T3Ct1 = new Condition(t3, "y1r2r3", TransitionCondition.NotNull);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition= T3Ct1;
        grdT3.Activations.add(new Activation(t3, "y1r2r3", TransitionOperation.Move, "r1g2r3"));
        grdT3.Activations.add(new Activation(t3, "red", TransitionOperation.SendOverNetwork, "OP4"));
        grdT3.Activations.add(new Activation(t3, "green", TransitionOperation.SendOverNetwork, "OP5"));
        grdT3.Activations.add(new Activation(t3, "Ten", TransitionOperation.DynamicDelay,""));
        t1.GuardMappingList.add(grdT3);


        pn.Transitions.add(t3);

        //----------------------------T4------------------------------------
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T4";
        t4.InputPlaceName.add("r1g2r3");


        Condition T4Ct1 = new Condition(t4, "r1g2r3", TransitionCondition.NotNull);
        Condition T4Ct2 = new Condition(t4, "in5", TransitionCondition.NotNull);
        T4Ct1.SetNextCondition(LogicConnector.AND, T4Ct2);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition= T4Ct1;
        grdT4.Activations.add(new Activation(t4, "r1g2r3", TransitionOperation.Move, "r1y2r3"));
        grdT4.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP5"));
        grdT4.Activations.add(new Activation(t4, "Ten", TransitionOperation.DynamicDelay,""));


        Condition T4Ct3 = new Condition(t4, "r1g2r3", TransitionCondition.NotNull);
        Condition T4Ct4 = new Condition(t4, "in5", TransitionCondition.IsNull);
        T4Ct3.SetNextCondition(LogicConnector.AND, T4Ct4);

        GuardMapping grdT42 = new GuardMapping();
        grdT42.condition= T4Ct3;
        grdT42.Activations.add(new Activation(t4, "r1g2r3", TransitionOperation.Move, "r1y2r3"));
        grdT42.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP6"));
        grdT42.Activations.add(new Activation(t4, "Five", TransitionOperation.DynamicDelay,""));

        t4.GuardMappingList.add(grdT4);
        t4.GuardMappingList.add(grdT42);


        pn.Transitions.add(t4);


        //----------------------------T5------------------------------------
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "T5";
        t5.InputPlaceName.add("r1y2r3");


        Condition T5Ct1 = new Condition(t2, "r1y2r3", TransitionCondition.NotNull);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition= T5Ct1;
        grdT5.Activations.add(new Activation(t5, "r1y2r3", TransitionOperation.Move, "r1r2g3"));
        grdT5.Activations.add(new Activation(t5, "green", TransitionOperation.SendOverNetwork, "OP6"));
        grdT5.Activations.add(new Activation(t5, "red", TransitionOperation.SendOverNetwork, "OP5"));
        grdT5.Activations.add(new Activation(t5, "Five", TransitionOperation.DynamicDelay,""));

        t5.GuardMappingList.add(grdT5);


        pn.Transitions.add(t5);

        //----------------------------T6------------------------------------
        PetriTransition t6 = new PetriTransition(pn);
        t6.TransitionName = "T6";
        t6.InputPlaceName.add("r1r2g3");

        Condition T6Ct1 = new Condition(t6, "r1r2g3", TransitionCondition.NotNull);
        Condition T6Ct2 = new Condition(t6, "in6", TransitionCondition.NotNull);
        T6Ct1.SetNextCondition(LogicConnector.AND, T6Ct2);

        GuardMapping grdT6 = new GuardMapping();
        grdT6.condition= T6Ct1;
        grdT6.Activations.add(new Activation(t6, "r1r2g3", TransitionOperation.Move, "r1r2y3"));
        grdT6.Activations.add(new Activation(t6, "yellow", TransitionOperation.SendOverNetwork, "OP6"));
        grdT6.Activations.add(new Activation(t6, "Ten", TransitionOperation.DynamicDelay,""));

        Condition T6Ct3 = new Condition(t6, "r1r2g3", TransitionCondition.NotNull);
        Condition T6Ct4 = new Condition(t6, "in6", TransitionCondition.IsNull);
        T6Ct3.SetNextCondition(LogicConnector.AND, T6Ct4);

        GuardMapping grdT62 = new GuardMapping();
        grdT62.condition= T6Ct3;
        grdT62.Activations.add(new Activation(t6, "r1r2g3", TransitionOperation.Move, "r1r2y3"));
        grdT62.Activations.add(new Activation(t6, "yellow", TransitionOperation.SendOverNetwork, "OP5"));
        grdT62.Activations.add(new Activation(t6, "Five", TransitionOperation.DynamicDelay,""));

        t6.GuardMappingList.add(grdT6);
        t6.GuardMappingList.add(grdT62);


        pn.Transitions.add(t6);

        //----------------------------T7------------------------------------

        PetriTransition t7 = new PetriTransition(pn);
        t7.TransitionName = "T7";
        t7.InputPlaceName.add("r1r2y3");

        Condition T7Ct1 = new Condition(t7, "r1r2y3", TransitionCondition.NotNull);

        GuardMapping grdT7 = new GuardMapping();
        grdT7.condition= T7Ct1;
        grdT7.Activations.add(new Activation(t7, "r1r2y3", TransitionOperation.Move, "r1r2r3"));
        grdT7.Activations.add(new Activation(t7, "red", TransitionOperation.SendOverNetwork, "OP6"));
        grdT6.Activations.add(new Activation(t7, "Ten", TransitionOperation.DynamicDelay,""));


        t7.GuardMappingList.add(grdT7);


        pn.Transitions.add(t7);

        // -------------------------------------------------------------------------------------
        // ----------------------------PN Start-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Exp1 started \n ------------------------------");
        pn.Delay = 2000;
        // pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);





    }
}
