package dnaObjects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import neuralObjects.LessThanProcessor;
import neuralObjects.LinearVelocitySensor;
import neuralObjects.Neuron;
import neuralObjects.PushAffector;
import neuralObjects.TurnCCWAffector;

import modelObjects.CreatureModel;

/**
 * This class serves as a container for NeuronLinks and NeuronTemplates.
 * @author mehul
 *
 */
public class Dna {
	private LinkedList<NeuronTemplate> neuronTemplateList = new LinkedList<NeuronTemplate>();
	private LinkedList<NeuronLink> neuronLinkList = new LinkedList<NeuronLink>();
	
	/**
	 * Reads in a file's content and sets up the list of neuron templates and neuron links
	 * @param fileContents the complete contents of the input file
	 */
	public Dna(String filePath){
		Scanner s = null;
		try{
			s = new Scanner(new BufferedReader(new FileReader(filePath)));
			
			while(s.hasNext()){
				String str = s.nextLine();
				//Checking to see if this is just whitespace.
				if(str.trim().length() > 0)
					processDNAInstruction(str);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not load file: " + filePath);
		}finally {
			if (s != null) {
				s.close();
			}
		}
	}
	
	/**
	 * Used to create an empty DNA
	 */
	public Dna(){	
	}
	
	/**
	 * Returns a perfect copy of this dna. Note that this copying process does
	 * not require getting a newer copy if this piece of DNA
	 * @return
	 */
	public Dna getPerfectCopy(){
		Dna copyDna = new Dna();
		//Copying neuronTemplates
		for(NeuronTemplate nT: this.neuronTemplateList){
			copyDna.neuronTemplateList.add(nT.getPerfectCopy());
		}
		
		//Copying neuronLinks 
		for(NeuronLink nl: this.neuronLinkList){
			copyDna.neuronLinkList.add(nl.getPerfectCopy());
		}
		
		return copyDna;
	}

	/**
	 * Processes a single DNA instruction and adds neuronTemplate or link.
	 * @param line
	 */
	public void processDNAInstruction(String line){
		line = line.toLowerCase();
		int firstSpaceChar = line.indexOf(" ");
		String instType = line.substring(0, firstSpaceChar);
		String restOfInst = line.substring(firstSpaceChar+1);
		
		//Check to see if this is a neuron instruction or neuronLink instruction
		if(instType.equals("neuron")){
			NeuronTemplate newTemplate = NeuronTemplate.buildNeuronTemplate(restOfInst);
			if (newTemplate != null)
				neuronTemplateList.add(newTemplate);
		} else if (instType.equals("neurallink")) {
			LinkedList<NeuronLink> neuronLinks = NeuronLink.buildNeuronLink(restOfInst);
			if(neuronLinks != null)
				neuronLinkList.addAll(neuronLinks);
		} else {
			System.err.println("Could not parse the following line: "+line);
		}
	}

	public void addNeuronTemplate(NeuronTemplate nt){
		neuronTemplateList.add(nt);
	}
	
	public void addNeuronLink(NeuronLink nl){
		neuronLinkList.add(nl);
	}
	
	/**
	 * Sets up a creature's neural network
	 * @param creature
	 */
	public void setupNeuralNetwork(CreatureModel creature){
		//This variable stores a list of all the neurons, just to facilitate looking for a particular Neuron by its ID
		LinkedList<Neuron> fullNeuronList = new LinkedList<Neuron>();  
		
		//**Creating all the neurons
		for(NeuronTemplate neuronTemplate: neuronTemplateList){
			Neuron newNeuron = null;
			
			switch (neuronTemplate.myType){
			//Doing all the Sensor Neurons
			case SENSOR_LINEAR_VEL:
				newNeuron = new LinearVelocitySensor(neuronTemplate.id, creature);
				creature.sensorNeuronList.add(newNeuron);
				break;
			
			//Doing all the Processor Neurons
			case PROC_LESSTHAN:
				newNeuron = new LessThanProcessor(neuronTemplate.id, neuronTemplate.values[0]);
				break;
			
			//Doing all the Affector neurons
			case AFFECTOR_PUSH:
				newNeuron = new PushAffector(neuronTemplate.id, creature, neuronTemplate.values[0]);
				creature.affectorNeuronList.addLast(newNeuron);
				break;
			case AFFECTOR_TURN_CCW:
				newNeuron = new TurnCCWAffector(neuronTemplate.id, creature, neuronTemplate.values[0]);
				creature.affectorNeuronList.addLast(newNeuron);
				break;
			default:
				break;
			}
			
			if(newNeuron != null)
				fullNeuronList.addLast(newNeuron);
			else
				System.err.println("Did not know how to add the following neuron to the creature's neural network: "+neuronTemplate);
		}
		
		//**Linking all the neurons
		for (NeuronLink neuronLink : neuronLinkList){
			Neuron fromNeuron = Neuron.getNeuronByID(fullNeuronList, neuronLink.fromID);
			if(fromNeuron == null)
				continue; /* Quietly ignore skip this link because this will probably become
				a common occurance when full blown evolution occurs*/
			
			Neuron toNeuron = Neuron.getNeuronByID(fullNeuronList, neuronLink.toID);
			if(toNeuron == null)
				continue; /* Quietly ignore skip this link because this will probably become
				a common occurance when full blown evolution occurs*/
			
			/*Adding the connection
			The toNeuron will call call the fromNeuron during
			processing
			*/
			toNeuron.addNeuralInput(fromNeuron);
		}
		
		//TODO just for testing
		
//		//Add always on push affector
//		AffectorNeuron n_push = new PushAffector("", this, 8*1/60f);
//		n_push.value = 1f;
//		n_push.wakeUp();
//		affectorNeuronList.addLast(n_push);
//		
//		//adding linear velocity sensor -> less than processor -> Turn CCW Affector
//		Neuron n_sens = new LinearVelocitySensor("", this);
//		Neuron n_proc = new LessThanProcessor("", 1.5f);
//		Neuron n_turn_ccw = new TurnCCWAffector("", this, 120*1/60f);
//		
//		affectorNeuronList.addLast(n_push);
//		sensorNeuronList.add(n_sens);
//		n_turn_ccw.addNeuralConnection(n_proc);
//		n_proc.addNeuralConnection(n_sens);
//		affectorNeuronList.add(n_turn_ccw);
	}
}
