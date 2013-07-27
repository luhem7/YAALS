# Introduction #

Here you can find out information about the neurons that control the actions and reactions of each creature. This describes how each Neuron is declared in the DNA, how each type of Neuron functions and a general overview of how Neurons are processed in each timestep. This document will be updated as new pieces of functionality are added.

#The general type of Neuron #

## Overview ##

The following is what each Neuron *knows*:

1. _id_ - unique string - The name of this neuron
2. _value_ - float - The value stored in the neuron. This value is used in different ways depending on the type of the Neuron. Tight now, this single value is the information that is passed around from one Neuron to another.
3. _myType_ - The type of Neuron it is.
4. _connectionsList_ - The list of neurons that this Neuron gets information __from__. What this means is that each Neuron does not know where its "value" goes to!

In each timestep in the simulation, each Neuron gathers the values from its neural inputs, performs some calculations based on this input and stores the result in itself. This result can be used further on later by other neurons.

## General Structure of DNA ## 

Dna instructions come in two flavors: Neural Template instructions and Neural Link instructions. Neural Template instructions specify the name, value and type of each neuron. Depending on the type of the neuron, a neural template instruction may also be followed by a list of numbers that are used in fleshing out that neuron (further info below)