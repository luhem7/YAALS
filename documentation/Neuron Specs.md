# Introduction #

Here you can find out information about the neurons that control the actions and reactions of each creature. This describes how each Neuron is declared in the DNA, how each type of Neuron functions and a general overview of how Neurons are processed in each timestep. This document will be updated as new pieces of functionality are added.

#The general type of Neuron #

## Overview ##

The following is what each Neuron *knows*:

1. _id_ - unique string - The name of this neuron
2. _output_ - float - The value outputed by this neuron after it has processed its inputs.
3. _myType_ - The type of Neuron it is.
4. _inputsList_ - The list of neurons that this Neuron gets information __from__. What this means is that each Neuron does not know where its output goes to!
6. _attributes_ - Neurons may have extra attributes that are used by that neuron during processing. Attributes are usually fixed and defined in the DNA itself. 

In each timestep in the simulation, each Neuron gathers the values from its neural inputs, performs some calculations based on this input and stores the ouput in itself. This output can be used further on later by other neurons.

## General Structure of DNA

Dna instructions come in two flavors: Neural Template instructions and Neural Link instructions. Neural Template instructions specify the name, output and type of each neuron. Depending on the type of the neuron, a neural template instruction may also be followed by a list of numbers that are used in fleshing out that neuron (further info below). There is only one kind of neuron link instruction. The general format is as follows:

> NeuronId1 -> NeuronId2 -> NeuronId3 -> ...

The arrows represent the flow of information from one neuron to another.

### Categories of Neurons
There are three broad categories of neurons:
  1. Sensor type: These types of neurons don't process inputs from other neurons. Usually this type of Neuron processes some particular creature state and outputs the result of that processing. For example, a sensor neuron that just outputs the current velocity of the creature.
  2. Processor type: These types of neurons have both inputs and a single output. For example, a processor neuron that just compares two inputs.
  3. Affector type: These types of neurons process neural inputs, and they may also have an output. But generally speaking, they perform some kind of action upon the creature. For example, a affector neuron that pushes the creature forward when it is activated. Every affector neuron usually uses an additional attribute called recoveryPeriod. This is the time in seconds in takes for a a neuron recover after firing once. 
  

## How to write DNA
Well, first you need to figure out what your creature does. TODO: Flesh out this tutorial

# Types of Neurons #

## Linear Velocity Sensor Neuron
### Description 
This neuron is a sensor type. It outputs the linear velocity of the creature without doing any processing on it.
### Syntax
> neuron <string: neuronId> lin_vel_sen

> Example: neuron VelocitySensor lin_vel_sen

## Blinker Neuron
### Description
This does not process any inputs. It outputs a 1 signal based on its frequency attribute. Other wise the neuron outputs a zero.
### Syntax
> neuron <string: neuronId> blinker_sen <float: frequency in Hz>

> neuron Blinker1 blinker_sen 0.5

Note that because of simulation limitations, the maximum frequency is about 60 Hz. 

## Less than Processor Neuron
### Description 
This neuron can do one of two things depending on how it is declared:
  1. If it only has one input and if that input is less than the threshold (a neural attribute), then the neuron outputs a 1. Otherwise it outputs a zero.
  2. If it has more than one input and if the first input is less than the second input, the neuron outputs a 1. Otherwise it outputs a zero.
  
### Syntax
> neuron <string: neuronId> less_than_proc <float: threshold>

> Example: neuron LessThanProc less_than_proc 1

## Push Affector Neuron
### Description 
If this neuron's first input is greater than 1, it moves the creature in the direction it is facing. If this neuron's first input is less -1, it moves the creature backwards in the direction it is facing. Otherwise, it does nothing. 
### Syntax
> neuron <string: neuronId> push_aff <float: recoveryPeriod>

> Example: neuron Pusher push_aff 0.5

RecoveryPeriod is the time is the time in seconds in takes for a a neuron recover after firing once. 

## Turn CCW processor
### Description 
If this neuron's first input is greater than 1, it rotates the creature counter clockwise. If this neuron's first input is less -1, it moves the creature clockwise. Otherwise, it does nothing.
### Syntax
> neuron <string: neuronId> turn_ccw_aff <float: neuronSleepPeriod>

> neuron Turner turn_ccw_aff 0.5

RecoveryPeriod is the time is the time in seconds in takes for a a neuron recover after firing once. 

## Neuron

### Description 

### Syntax