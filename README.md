# OpenAPE

## Introduction 

OpenAPE is a settings-based personalization framework to cover three important scenarios:

The bread and butter scenario is to **infer settings** for an application a user might not have met yet, for example using an Android phone for the first time with only using Windows PCs so far. In this use case, OpenAPE will try to find other users who have similar settings, but also have settings for the new target device or application. To do so, OpenAPE deploys various machine learning techniques, such as clustering existing users and creating virtual user profiles for these clusters. The fundamental assumption here is that users will adapt their device to their needs or preferences and thus settings applied by a users can be considered verified.

Quite similar to settings-adaptation scenario is **inferring content**. When coupled with a content server, OpenAPE can deliver content for webpages, such as images, that match a users settings, for example delivering a high-contrast image if respective settings are present for screen readers or magnifiers. There are, however, some subtle differences: it is hard to assess whether the content delivered was actually a good choice in the current situation. Further, content nowadays can range from simple images up to whole web apps, which in turn will invoke their own adaptation queries.

Finally, as a mix of the above use cases, OpenAPE could be used to **transfer rules and scripts to a user**, based on their preferences. Consider, for example, a smart home where the configuration for dimming lights in the evening or altering the lighting colour are complex, even time-dependent scripts. These scripts could be shipped to a user similar to simple content, but they have many configuration dimensions themselves, such as scaling with preferred light intensity etc.

## Licence and Copyright 

Copyright: 2016-2020 Research group REMEX, Hochschule der Medien (Stuttgart, Germany).

Licence: [Apache 2.0](https://github.com/REMEXLabs/OpenAPE/blob/master/openAPE/license.txt).

## Funding Acknowledgement
The research leading to these results has received funding from the European
Union's Seventh Framework Programme (FP7) under grant agreement No.610510
([Prosperity4all](http://www.prosperity4all.eu/)).