﻿using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using RAIN.Core;
using RAIN.Belief;
using RAIN.Action;

public class _TemplateName_ : Action
{
    public _TemplateName_()
    {
        actionName = "_TemplateName_";
    }

    public override ActionResult Start(Agent agent, float deltaTime)
    {
        return ActionResult.SUCCESS;
    }

    public override ActionResult Execute(Agent agent, float deltaTime)
    {
        return ActionResult.SUCCESS;
    }

    public override ActionResult Stop(Agent agent, float deltaTime)
    {
 	     return ActionResult.SUCCESS;
    }
}