# SysML in Arrowhead Tools

This repository contains the *Arrowhead MagicDraw Plugin* developed by IncQuery Labs.

The purpose of this plugin is to provide
* a reference realization of the *Arrowhead SysML Profile* (see later),
* and a collection of MagicDraw-specific features for advanced features and enhanced user experience on this platform.

Here, we treat MagicDraw and Cameo Systems Modeler equivalently (the latter being MagicDraw with integrated SysML support, which can be individually installed for MagicDraw as well), and refer to both tools as MagicDraw. Development and testing was performed using Cameo Systems Modeler 19.0 SP3.

This repository focuses on leveraging SySML technology within the Arrowhead Tools project; for more information 
on the overall goals and approach of Arrowhead Tools, see the [official website](https://www.arrowhead.eu/arrowheadtools).

## Arrowhead SysML profile

The Arrowhead SysML Profile in MagicDraw (.mdzip) format comes bundled with the [plugin](https://github.com/IncQueryLabs/arrowhead-tools/tree/master/Arrowhead%20Magicdraw%20Plugin).

You can locate it in the profiles folder of either the provided .zip plugin or your local MagicDraw installation, after importing the plugin (see below).

For a detailed description of the concepts of the profile itself, refer to its [documentation](https://github.com/IncQueryLabs/arrowhead-tools/tree/master/Profile%20Documentation), automatically generated from MagicDraw using [DocGen](https://github.com/Open-MBEE/mdk/).

## Advanced MagicDraw Features provided by the plugin

To install the plugin, follow the [MagicDraw plugin import instructions](https://docs.nomagic.com/display/NMDOC/Installing+plugins).

### Export to AHX

_AHX (ArrowHead eXchange)_ is a slim JSON data format (sometimes also called a _semantics_) for providing a _toolchain_ integrating SysML models (representing design-time considerations) with running Arrowhead local clouds via their management interface.

The Export feature can be simply invoked from MagicDraw by clicking on File -> Export -> AHX File.

An excerpt of an example AHX JSON:

```json
[
  {
    "systems": [
      {
        "systemName": "Forecaster1",
        "address": "0.0.0.0",
        "port": 1
      },
      {...}
    ],
    "serviceRegistryEntries": [
      {
        "serviceDefinition": "SenML EnergyForecast",
        "provider": {
          "systemName": "Forecaster1",
          "address": "0.0.0.0",
          "port": 1
        },
        "secure": "NOT_SECURE",
        "interfaces": [
          "HTTP-INSECURE-JSON"
        ],
        "metadata": {}
       },
       {...}
    ],
    "authRules": [
      {
        "consumer": "Forecaster1",
        "serviceDefinition": "SenML OutdoorData",
        "providers": [
          "OutdoorSensor"
        ],
        "interfaces": [
          "HTTP-INSECURE-JSON"
        ]
      },
      {...}
    ]
  }
]
```

### Import from AHX

With some limitations (not to be detailed here), the plugin supports the re-integration of some changes occurring outside MagicDraw.

For example, consider changing a port number on some system in the management interface and exporting it into AHX again (the management tool also supports AHX). Then, using File -> Import -> AHX File, the new model contents are parsed and the changed port number even gets higlighted in MagicDraw!

### Documentation generation

To read more about our documentation approach, visit our first example, the [documentation of the profile itself](https://github.com/IncQueryLabs/arrowhead-tools/tree/master/Profile%20Documentation). We intend to extend this flexible approach to automated documentation scenarios for industrial use-cases as well.

### Validation

Using [IncQuery Labs' advanced MagicDraw validation add-on](https://incquery.io/incquery-desktop/), we are able to provide automatic, active validation to check the well-formedness of SysML models _during their creation_. Although the validation suite is far from being formally complete, some showcase examples are implemented and active, as shown in the screenshot below:


