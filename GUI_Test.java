//imports
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class GUI_Test extends Application {
	//Layout
		private GridPane gp;
		//Conversion
		private Label lb_conType;
		private ComboBox<String> combobox;
		// Imperial
		private Label lb_imperial;
		private TextField tf_imperial;
		private ToggleGroup tg_imperial;
		private RadioButton rb_imperial_1;
		private RadioButton rb_imperial_2;
		private RadioButton rb_imperial_3;
		// Metric
		private Label lb_metric;
		private TextField tf_metric;
		private ToggleGroup tg_metric;
		private RadioButton rb_metric_1;
		private RadioButton rb_metric_2;
		private RadioButton rb_metric_3;
		//Accuracy
		private Label lb_accuracy;
		private Slider sl_accuracy;
		private TextField tf_accuracy;
		private Double accuracy;	
		//Convert and Erase
		private Button convert;
		private Button erase;
		//Conversion Map
		private Map<String,Double> conversion_map = new LinkedHashMap<String, Double>();
		{ 
			conversion_map.put("in",0.0254); // key:in value:the factor required to convert inches to metres
			conversion_map.put("foot",0.3048);
			conversion_map.put("yard",0.9144);
			conversion_map.put("mm",1000.0); // key:mm value:the factor required to then convert metres to mm
			conversion_map.put("cm",100.0);
			conversion_map.put("m",1.0);
			conversion_map.put("oz",0.0283);
			conversion_map.put("lb",0.453);
			conversion_map.put("stone",6.35);
			conversion_map.put("g",1000.0);
			conversion_map.put("kg",1.0);
			conversion_map.put("ton",0.001);
		}
		public void init() {
			gp = new GridPane();
			//Conversion
			lb_conType = new Label("Conversion Type");
			combobox = new ComboBox<String>();
			combobox.getItems().addAll("Length","Mass");
			combobox.setValue("Length");
			
			gp.setHgap(8);
			gp.setVgap(8);
			gp.setPadding(new Insets(10, 10, 10, 10));
			//imperial
			lb_imperial = new Label("Imperial");
			tf_imperial = new TextField();
			tf_imperial.setText("1.0");
			tg_imperial = new ToggleGroup();
			rb_imperial_1 = new RadioButton();
			rb_imperial_2 = new RadioButton();
			rb_imperial_3 = new RadioButton();
			rb_imperial_1.setToggleGroup(tg_imperial);
			rb_imperial_2.setToggleGroup(tg_imperial);
			rb_imperial_3.setToggleGroup(tg_imperial);
			rb_imperial_1.setSelected(true);
			//Metric
			lb_metric = new Label("Metric");
			tf_metric = new TextField();
			tf_metric.setEditable(false);
			tg_metric = new ToggleGroup();
			rb_metric_1 = new RadioButton();
			rb_metric_2 = new RadioButton();
			rb_metric_3 = new RadioButton();
			rb_metric_1.setToggleGroup(tg_metric);
			rb_metric_2.setToggleGroup(tg_metric);
			rb_metric_3.setToggleGroup(tg_metric);
			rb_metric_1.setSelected(true);
			//Accuracy
			lb_accuracy = new Label("Accuracy");
			sl_accuracy = new Slider();
			sl_accuracy.setMin(0);
			sl_accuracy.setMax(6);
			sl_accuracy.setValue(4);
			sl_accuracy.setShowTickMarks(true);
			sl_accuracy.setShowTickLabels(true);
			sl_accuracy.setMajorTickUnit(1);
			sl_accuracy.setBlockIncrement(0.5);
			accuracy = sl_accuracy.getValue();
			tf_accuracy = new TextField(accuracy.toString());
			tf_accuracy.setEditable(false);
			//Buttons
			convert = new Button ("Convert");
			erase = new Button ("Erase");
			
			gp.add(lb_conType, 0, 0);
			gp.add(combobox, 1, 0, 3, 1);
			gp.add(lb_imperial, 0, 1);
			gp.add(tf_imperial, 1, 1, 3, 1);
			gp.add(lb_metric, 5, 1);
			gp.add(tf_metric, 6, 1, 3, 1);
			gp.add(rb_imperial_1, 1, 2, 1, 1);
			gp.add(rb_imperial_2, 2, 2, 1, 1);
			gp.add(rb_imperial_3, 3, 2, 1, 1);
			gp.add(rb_metric_1, 6, 2, 1, 1);
			gp.add(rb_metric_2, 7, 2, 1, 1);
			gp.add(rb_metric_3, 8, 2, 1, 1);
			gp.add(lb_accuracy, 0, 3);
			gp.add(sl_accuracy, 1, 3, 4, 1);
			gp.add(tf_accuracy, 6, 3, 3, 1);
			gp.add(convert, 0, 7);
			gp.add(erase, 1, 7);
			
			initalizeControlValues();
			convert();
			//Listener for slider
			sl_accuracy.valueProperty().addListener(new ChangeListener<Number>(){
				public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue){
					DecimalFormat d = new DecimalFormat("0");
					accuracy = newValue.doubleValue();
					tf_accuracy.setText((d.format(accuracy)).toString());
					convert();
				}
			});
			//Listener for imperial toggle group
			this.tg_imperial.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle,Toggle new_toggle) {
					convert();						
				}
			});
			//Listener for metric toggle group
			this.tg_metric.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle,Toggle new_toggle) {
					convert();
				}
			});
			//Listener for comboBox
			combobox.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					initalizeControlValues();
				}
			});
			//Listener for convert button
			convert.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event) {
					convert();
				}
			});
			//Listener for erase Button
			erase.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					initalizeControlValues();
					convert();
				}
			});
		}
		public void start(Stage primaryStage) {
			primaryStage.setTitle("Unit Converter");
			primaryStage.setScene(new Scene(gp, 500, 250));
			primaryStage.show();
		}
		public void stop() {}
		public static void main(String[] args) {
			launch(args);
		}
		private void convert() {
			Double value_imperial;
			try {
				String imperial_value = tf_imperial.getText(); 
				value_imperial = Double.parseDouble(imperial_value);
				//Radio button key
				String key_imperial = ((RadioButton)this.tg_imperial.getSelectedToggle()).getText();
				String key_metric = ((RadioButton)this.tg_metric.getSelectedToggle()).getText();
				
				Double conv_imperial = this.conversion_map.get(key_imperial);
				Double conv_metric = this.conversion_map.get(key_metric);
				
				// Calculate the conversion value for metric
				Double value_metric = value_imperial * conv_metric* conv_imperial;
				
				//Display new value
				accuracy = (double) Math.round(accuracy);
				DecimalFormat d = new DecimalFormat();
				if(accuracy == 0) {
					d = new DecimalFormat("0"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 1) {
					d = new DecimalFormat("0.0"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 2) {
					d = new DecimalFormat("0.00"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 3) {
					d = new DecimalFormat("0.000"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 4) {
					d = new DecimalFormat("0.0000");
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 5) {
					d = new DecimalFormat("0.00000"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
				if(accuracy == 6) {
					d = new DecimalFormat("0.000000"); 
					tf_metric.setText(d.format(value_metric).toString());
				}
			}
			catch(NumberFormatException e){
				tf_metric.setText("");
				tf_imperial.setText("Please insert a number");
			}
		}
		private void initalizeControlValues() {
			Object[] keys = this.conversion_map.keySet().toArray();
			if(combobox.getValue()=="Length"){
				//Initialize length to 1.0
				tf_imperial.setText("1.0");
				// Set slider scale 0 to 6, set slider value to 4 and ticks to 1 unit intervals
				sl_accuracy.setMin(0);
				sl_accuracy.setMax(6);
				sl_accuracy.setValue(4);
				sl_accuracy.setShowTickMarks(true);
				sl_accuracy.setShowTickLabels(true);
				sl_accuracy.setMajorTickUnit(1);
				sl_accuracy.setBlockIncrement(0.5);
				// Initialize the radio buttons
				rb_imperial_1.setText((String)keys[0]);
				rb_imperial_2.setText((String)keys[1]);
				rb_imperial_3.setText((String)keys[2]);
				rb_metric_1.setText((String)keys[3]);
				rb_metric_2.setText((String)keys[4]);
				rb_metric_3.setText((String)keys[5]);	
				// Set default selections for the radio buttons
				rb_imperial_1.setSelected(true);
				rb_metric_1.setSelected(true);
			}
			else{
				// Initialize the mass to .5
				tf_imperial.setText("0.5");
				// Set slider scale 0 to 5, set slider value to 3 and ticks to 1 unit intervals
				sl_accuracy.setMin(0);
				sl_accuracy.setMax(5);
				sl_accuracy.setValue(3);
				sl_accuracy.setShowTickMarks(true);
				sl_accuracy.setShowTickLabels(true);
				sl_accuracy.setMajorTickUnit(1);
				sl_accuracy.setBlockIncrement(0.5);
				// Initialize the radio buttons
				rb_imperial_1.setText((String)keys[6]);
				rb_imperial_2.setText((String)keys[7]);
				rb_imperial_3.setText((String)keys[8]);
				rb_metric_1.setText((String)keys[9]);
				rb_metric_2.setText((String)keys[10]);
				rb_metric_3.setText((String)keys[11]);	
				// Set default selections for the radio buttons
				rb_imperial_2.setSelected(true);
				rb_metric_2.setSelected(true);
			}
		}
}
